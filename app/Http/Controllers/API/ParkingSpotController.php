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
            'claimed' => 'required|boolean'
        ]);

        $parking_spot = new ParkingSpot([
            'town' => $request->town,
            'address' => $request->address,
            'parkingNumber' => $request->parkingNumber,
            'latitude' => $request->latitude,
            'longitude' => $request->longitude,
            'user_id' => \auth('api')->id(),
            'claimed' =>  $request->claimed

        ]);

        $parking_spot->save();
        return response()->json([
        //    "message" => "Parking spot registered successfully",
            "parking" => $parking_spot
        ], 201);

    }

    /**
     * @param ParkingSpot $parkingSpot
     * @return array
     */


    public function show(Request $request)
    {

        $id=auth('api')->user()->id;
        $con = mysqli_connect("localhost", "root", "", "parkingdb");
        $sql=DB::table('parking_spots')
            ->select('parking_spots.id', 'parking_spots.address',
                'parking_spots.claimed', 'parking_spots.town', 'parking_spots.parkingNumber')
            ->join('users','parking_spots.user_id','=','users.id')
            ->where('parking_spots.user_id', $id)
            ->get();
return $sql;
//        $final_result=[
//            // "id"=>$row["id"],
//            "town"=>$sql["town"],
//            "email"=>$sql["email"],
//            "username"=>$sql["username"],
//            "phone"=>$sql["phone"]
//        ];
//        return $final_result;
    }


    public function index(Request $request)
    {
        $perPage = $request->per_page ?? null;
        return ParkingResource::collection(ParkingSpot::paginate($perPage));
    }


//    public function store(ParkingRequests $request): ParkingResource
//    {
//        $createData['town'] = $request->town;
//        $createData['address'] = $request->address;
//        $createData['parkingNumber'] = $request->parkingNumber;
//        $createData['latitude'] = $request->latitude;
//        $createData['longitude'] = $request->longitude;
//        $createData['user_id'] = auth('api')->user()->id;
//
//        $parking = ParkingSpot::create($createData);
//
//        return new ParkingResource($parking);
//    }

//    public function destroy(ParkingSpot $parkingSpot)
//    {
//
//        $id=auth('api')->user()->id;
//        DB::table('parking_spots')
//            ->select('parking_spots.id')
//            ->join('users','parking_spots.user_id','=','users.id')
//            ->where('parking_spots.user_id', $id)
//            ->delete();
//
//        return response()-> json(
//            ['message' => 'ParkingSpot(s) deleted'],200);
//    }

    public function updateParkingSpot(Request $request)
    {

        $request->validate([
            'parkingNumber' => 'required|string',
        ]);

        $id=auth('api')->user()->id;

        $con = mysqli_connect("localhost", "root", "", "parkingdb");
        $sql =
            "SELECT parking_spots.id
             FROM parking_spots
             JOIN users ON parking_spots.user_id=users.id
             WHERE parking_spots.user_id= '$id'
            ";
        $result = mysqli_query($con, $sql);
        $row = mysqli_fetch_array($result);

       // dd($row);

        $result1 = ParkingSpot::find($row['id']);

        $result1->parkingNumber = $request->parkingNumber;

            $result1->save();
        if ($result1->save()) {
            return response()->json([
                "message" => "ParkingNumber was updated."
            ], 201);
        } else {
            return response()->json([
                "message" => "ParkingNumber update failed"
            ], 401);
        }
    }
    public function destroy(Request $request)
    {

        $id= $request->id;
        $parking = ParkingSpot::find($id);
        $parking->delete();
        return response()->json([
            "message" => "ParkingSpot deleted successfully"
        ], 401);
    }
}
