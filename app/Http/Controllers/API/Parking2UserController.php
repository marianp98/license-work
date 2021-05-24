<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Models\Parking2User;
use Illuminate\Http\Request;

class Parking2UserController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth:api');
    }

    public function parking2user(Request $request)
    {
        $request->validate([
            'user_id' => 'required|string',
            'parkingspot_id' => 'required|integer',
            'availability_id' => 'required|string',
        ]);
        $parking2user = new Parking2User([
            'user_id' => $request->user_id,
            'parkingspot_id' => $request->parkingspot_id,
            'availability_id' => $request->availability_id,
        ]);

        $parking2user->save();
        return $parking2user;
//        return response()->json([
//            //    "message" => "Parking spot registered successfully",
//            "parking" => $parking2user
//        ], 201);
    }
}
