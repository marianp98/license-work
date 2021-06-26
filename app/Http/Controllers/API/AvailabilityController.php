<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Availability;
use Illuminate\Http\Request;

class AvailabilityController extends Controller
{

    public function __construct()
    {
        $this->middleware('auth:api');
    }

    public function dst(Request $request)
    {
        $request->validate([
            'id' => 'required|integer',
        ]);
        $availability = Availability::where('parkingspot_id', $request->id);
        $availability->delete();
        return response()->json([
            "message" => "Availability canceled"
        ], 201);
    }

    public function id_marking(Request $request)
    {
        $request->validate([
            'start_date' => 'required|date_format:Y-m-d H:i',
            'stop_date' => 'required|date_format:Y-m-d H:i',
            'id' => 'required|string',
        ]);

        $auth = \auth('api')->id();
        $con = mysqli_connect("localhost", "root", "", "parkingdb");
        $available_id = $request->id;
        $sql = "SELECT *
               FROM parking_spots
               WHERE id ='$available_id' ";


        $result = mysqli_query($con, $sql);
        $row = mysqli_fetch_array($result);

        $available = new Availability([
            'start_date' => $request->start_date,
            'stop_date' => $request->stop_date,
            'parkingspot_id' => $available_id,

        ]);
        $available->save();
        return $available;
    }
}


//public function destroy_availability(Availability $availability)
//{
//    $availability->delete();
//    return response()->json([
//        "message" => "Availability canceled"
//    ], 201);
//}

//public function marking(Request $request)
//{
//    $request->validate([
//        'start_date' => 'required|date_format:Y-m-d H:i',
//        'stop_date' => 'required|date_format:Y-m-d H:i',
//    ]);
//
//    $auth = \auth('api')->id();
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT parking_spots.id
//             FROM users
//             JOIN parking_spots ON users.id=parking_spots.user_id
// 	         WHERE (users.id = $auth)";
//    $result = mysqli_query($con, $sql);
//    $row = mysqli_fetch_array($result);
//
//    //dd($row["id"]);
//
//    $available = new Availability([
//        'start_date' => $request->start_date,
//        'stop_date' => $request->stop_date,
//        'parkingspot_id' => $row['id'],
//
//    ]);
//    $available->save();
//    return $available;
//    return response()->json([
//        'availability' => $available,
////            "message" => "Availability registered successfully"
//    ], 201);
//}


//public function destroy_id(Request $request)
//{
//    $request->validate([
//            'id' => 'required|string']
//    );
//    $id = $request->id;
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT availabilities.id, availabilities.parkingspot_id,availabilities.start_date,
//             availabilities.stop_date, parking_spots.claimed
//             FROM availabilities
//             JOIN parking_spots ON availabilities.parkingspot_id=parking_spots.id
//             JOIN users ON parking_spots.user_id=users.id
//             WHERE parking_spots.id='$id'";
//    $result = mysqli_query($con, $sql);
//    $row = mysqli_fetch_array($result);
//    return response()->json([
//        "message" => "Availability canceled"
//    ], 201);
//}


//public function show_unavailable_spots(Request $request)
//{
//    $request->validate([
//        'id' => 'required|string',
//    ]);
//
//    $id = $request->id;
//
//
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT availabilities.id, availabilities.parkingspot_id,availabilities.start_date,
//             availabilities.stop_date, parking_spots.claimed
//             FROM availabilities
//             JOIN parking_spots ON availabilities.parkingspot_id=parking_spots.id
//             JOIN users ON parking_spots.user_id=users.id
//             WHERE users.id='$id' AND NOW() < start_date OR NOW() > stop_date";
//    $result = mysqli_query($con, $sql);
//
//    $markers = array();
//    while ($row = mysqli_fetch_assoc($result)) {
//        $markers[] = $row;
//    }
//    return $markers;
//}


//public function show_available_spots(Request $request)
//{
//    $request->validate([
//        'id' => 'required|string',
//    ]);
//
//    $id = $request->id;
//
//
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT availabilities.id, availabilities.parkingspot_id,availabilities.start_date,
//             availabilities.stop_date, parking_spots.available, parking_spots.claimed
//             FROM availabilities
//             JOIN parking_spots ON availabilities.parkingspot_id=parking_spots.id
//             JOIN users ON parking_spots.user_id=users.id
//             WHERE users.id='$id' AND NOW() BETWEEN start_date AND stop_date ";
//    $result = mysqli_query($con, $sql);
//
//    $markers = array();
//    while ($row = mysqli_fetch_assoc($result)) {
//        $markers[] = $row;
//    }
//    return $markers;
//}


//
//public function availability_button()
//{
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT *
//             FROM availabilities
//             WHERE NOW() BETWEEN start_date AND stop_date";
//    $result = mysqli_query($con, $sql);
//    $markers = array();
//    while ($row = mysqli_fetch_assoc($result)) {
//        $markers[] = $row;
//    }
//    return $markers;
//}


//
//public function index()
//{
//
//    $con = mysqli_connect("localhost", "root", "", "parkingdb");
//    $sql =
//        "SELECT users.name, users.phone,
//             parking_spots.latitude,parking_spots.longitude,parking_spots.address,parking_spots.parkingNumber,
//             availabilities.start_date, availabilities.stop_date
//             FROM parking_spots
//             JOIN availabilities ON parking_spots.id=availabilities.parkingspot_id
//             JOIN users ON parking_spots.user_id=users.id
//             WHERE CURRENT_TIMESTAMP
//             BETWEEN availabilities.start_date
//             AND availabilities.stop_date";
//    $result = mysqli_query($con, $sql);
//    $markers = array();
//    while ($row = mysqli_fetch_assoc($result)) {
//        $markers[] = $row;
//    }
//    header('Content-Type:Application/json');
//    echo json_encode($markers);
//}
