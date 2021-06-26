<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Marker;
use App\Models\ParkingSpot;
use Illuminate\Http\Request;

class MarkerController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return void
     */
    public function index()
    {
        $con = mysqli_connect("localhost", "root", "", "parkingdb");

        $sql =
            "SELECT users.id as id_user, users.name, users.phone,
             parking_spots.id as parkingspotID, parking_spots.latitude,parking_spots.longitude,parking_spots.address,parking_spots.parkingNumber,parking_spots.claimed as claimed,parking_spots.address,
             availabilities.start_date, availabilities.stop_date, availabilities.id
             FROM parking_spots
             JOIN availabilities ON parking_spots.id=availabilities.parkingspot_id
             JOIN users ON parking_spots.user_id=users.id
             WHERE CURRENT_TIMESTAMP
             BETWEEN availabilities.start_date
             AND availabilities.stop_date";
        $result = mysqli_query($con, $sql);

        $markers = array();
        while ($row = mysqli_fetch_assoc($result)) {
            $markers[] = $row;
        }
        header('Content-Type:Application/json');

        echo json_encode($markers);

    }

    /**
     * Show the form for creating a new resource.
     *
     * @return void
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param Request $request
     * @return void
     */
    public function store(Request $request)
    {
        //
    }

    /**
     * Display the specified resource.
     *
     * @param Marker $marker
     * @return void
     */
    public function show(Marker $marker)
    {
        //
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param Marker $marker
     * @return void
     */
    public function edit(Marker $marker)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param Request $request
     * @param Marker $marker
     * @return void
     */
    public function update(Request $request, Marker $marker)
    {
        //
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param Marker $marker
     * @return void
     */
    public function destroy(Marker $marker)
    {
        $marker->delete();

        return response()->json([
            "message" => "Marker removed"
        ], 201);
    }


    public function claimed_update(Request $request)
    {
        $request->validate([
            'id' => 'required|string',
            'claimed' => 'required|int']);

        $id = $request->id;

        $result = ParkingSpot::find($id);


        $result->claimed = $request->claimed;
        $result->save();
        return $result;
    }

    public function available_update(Request $request)
    {
        $request->validate([
            'id' => 'required|string',
            'available' => 'required|int']);

        $id = $request->id;

        $result = ParkingSpot::find($id);


        $result->available = $request->available;
        $result->save();
        return $result;
    }

}
