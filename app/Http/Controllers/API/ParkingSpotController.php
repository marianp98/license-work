<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;

use App\Http\Resources\ParkingResource;
use App\Models\ParkingSpot;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class ParkingSpotController extends Controller
{

    public function __construct()
    {
        $this->middleware('auth:api');
    }

    public function parking(Request $request)
    {
        $request->validate([
            'town' => 'required|string',
            'parkingNumber' => 'required|integer',
            'address' => 'required|string',
            'latitude' => 'required|string',
            'longitude' => 'required|string',
            'available' => 'required|boolean',
            'claimed' => 'required|boolean',
            'start_date' => 'required|date_format:Y-m-d H:i',
            'stop_date' => 'required|date_format:Y-m-d H:i'
        ]);

        $parking_spot = new ParkingSpot([
            'town' => $request->town,
            'address' => $request->address,
            'parkingNumber' => $request->parkingNumber,
            'latitude' => $request->latitude,
            'longitude' => $request->longitude,
            'user_id' => \auth('api')->id(),
            'available' => $request->available,
            'claimed' => $request->claimed,
            'start_date' => $request->start_date,
            'stop_date' => $request->stop_date
        ]);
        $parking_spot->save();
        return response()->json([
            "message" => "Parking spot registered successfully",
            "parking" => $parking_spot
        ], 201);

    }

    /**
     * @param ParkingSpot $parkingSpot
     * @return array
     */
    public function show(Request $request)
    {
        $id = auth('api')->user()->id;
        mysqli_connect("localhost", "root", "", "parkingdb");
        $sql = DB::table('parking_spots')
            ->select('parking_spots.id', 'parking_spots.address'
                , 'parking_spots.parkingNumber', 'parking_spots.available', 'parking_spots.claimed', 'parking_spots.start_date', 'parking_spots.stop_date')
            ->join('users', 'parking_spots.user_id', '=', 'users.id')
            ->where('parking_spots.user_id', $id)
            ->get();
        return $sql;
    }

    public function destroy(Request $request)
    {
        $id = $request->id;
        $parking = ParkingSpot::find($id);
        $parking->delete();
        return response()->json([
            "message" => "ParkingSpot deleted successfully"
        ], 401);
    }


    public function updateParkingSpotDate(Request $request)
    {
        $request->validate([
            'id' => 'required|string',
            'start_date' => 'required|date_format:Y-m-d H:i',
            'stop_date' => 'required|date_format:Y-m-d H:i'
        ]);
        $id = $request->id;
        $result = ParkingSpot::find($id);
        $result->start_date = $request->start_date;
        $result->stop_date = $request->stop_date;

        if ($result->save()) {
            return response()->json([
                "message" => "ParkingSpot edit successfully"
            ], 201);
        } else {
            return response()->json([
                "message" => "ParkingSpot edit failed"
            ], 401);
        }
    }
}

//For future updates, maybe
//    public function updateParkingSpot(Request $request)
//    {
//
//        $request->validate([
//            'parkingNumber' => 'required|string',
//        ]);
//
//        $id = auth('api')->user()->id;
//
//        $con = mysqli_connect("localhost", "root", "", "parkingdb");
//        $sql =
//            "SELECT parking_spots.id
//             FROM parking_spots
//             JOIN users ON parking_spots.user_id=users.id
//             WHERE parking_spots.user_id= '$id'
//            ";
//        $result = mysqli_query($con, $sql);
//        $row = mysqli_fetch_array($result);
//
//        // dd($row);
//
//        $result1 = ParkingSpot::find($row['id']);
//
//        $result1->parkingNumber = $request->parkingNumber;
//
//        $result1->save();
//        if ($result1->save()) {
//            return response()->json([
//                "message" => "ParkingNumber was updated."
//            ], 201);
//        } else {
//            return response()->json([
//                "message" => "ParkingNumber update failed"
//            ], 401);
//        }
//    }
