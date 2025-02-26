package com.task.ubbar.presentation

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.task.ubbar.R
import com.task.ubbar.data.model.AddressRequestModel
import com.task.ubbar.data.model.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow


@AndroidEntryPoint
class MapFragment : Fragment() {
    private val viewModel: UbbarViewModel by viewModels()
    private lateinit var locationButton: AppCompatButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView
    private lateinit var mapController: IMapController
    private lateinit var selectGeoPoint: GeoPoint
    private lateinit var addressRequestModel: AddressRequestModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.map_fragment, container, false)
        locationButton = rootView.findViewById(R.id.locationButton)
        mapView = rootView.findViewById(R.id.mapView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<AddressRequestModel>(AddresKey)?.let {
            addressRequestModel = it
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission at runtime
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                12
            )
        } else {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            getCurrentLocation()
        }
        mapView.setMultiTouchControls(true)


        Configuration.getInstance().load(
            requireActivity().applicationContext,
            requireActivity().getSharedPreferences("osmdroid", MODE_PRIVATE)
        )
        locationButton.setOnClickListener {
            viewModel.selectPoint = selectGeoPoint
            addressRequestModel.lat = selectGeoPoint.latitude.toLong()
            addressRequestModel.lng = selectGeoPoint.longitude.toLong()
            viewModel.setAddress(addressRequestModel)
        }

        val mapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(point: GeoPoint?): Boolean {
                point?.let { selectedPoint ->
                    addMarker(selectedPoint)
                    selectGeoPoint = selectedPoint
                }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }

        val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)
        mapView.overlays.add(mapEventsOverlay)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().supportFragmentManager.popBackStack()
                }
            })


        viewModel.setAddressResponse.observe(viewLifecycleOwner) { result ->
            Log.d("Obbbaaaar", "setAddressResponse: $result")
            when (result) {
                is NetworkResult.Success -> {
                    requireActivity().supportFragmentManager.popBackStack()
                    requireActivity().supportFragmentManager.popBackStack()
                }

                is NetworkResult.NetworkError -> {}
                is NetworkResult.Loading -> {}
                is NetworkResult.HttpException -> {}
            }
        }
    }

    private fun addMarker(point: GeoPoint) {
        val marker = Marker(mapView)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "Selected Location"
        marker.snippet = "Lat: ${point.latitude}, Lon: ${point.longitude}"
        marker.infoWindow = MarkerInfoWindow(R.layout.map_fragment, mapView)

        mapView.overlays.removeIf { it is Marker }

        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLocation = GeoPoint(it.latitude, it.longitude)
                moveMapToLocation(userLocation)
            } ?: run {
                Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveMapToLocation(geoPoint: GeoPoint) {
        val mapController = mapView.controller
        mapController.setZoom(16.0) // Zoom level
        mapController.setCenter(geoPoint)

        // Add marker for current location
        val marker = Marker(mapView)
        marker.position = geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = "You are here"
        mapView.overlays.add(marker)

        mapView.invalidate() // Refresh map
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            12 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    Toast.makeText(requireContext(), "Permission Denied!", Toast.LENGTH_LONG).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        const val AddresKey = "addressRequestModel"
        fun newInstance(addressRequestModel: AddressRequestModel): MapFragment {
            return MapFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(AddresKey, addressRequestModel)
                }
            }
        }

    }

}