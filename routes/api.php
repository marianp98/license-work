<?php

use App\Http\Controllers\API\AvailabilityController;
use App\Http\Controllers\API\ParkingSpotController;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\MarkerController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::GET('/register', [UserController::class, 'register']);
Route::POST('/login', [UserController::class, 'login']);
Route::POST('/updateuser', [UserController::class, 'updateUser']);
Route::resource('user', UserController::class)->except('create', 'edit');
Route::POST('/destroyuser', [UserController::class, 'destroyUser']);
Route::GET('/showuser', [UserController::class, 'show']);
Route::GET('/token', [UserController::class, 'get_token']);
Route::POST('/updateemail', [UserController::class, 'updateEmail']);
Route::GET('/updatephone', [UserController::class, 'updatePhone']);



Route::GET('/store', [ParkingSpotController::class, 'store']);
Route::GET('/destroyparkings', [ParkingSpotController::class, 'destroy']);
Route::POST('/parking', [ParkingSpotController::class, 'parking']);
Route::GET('/updateparking', [ParkingSpotController::class, 'updateParkingSpot']);
Route::GET('/showparking', [ParkingSpotController::class, 'show']);
Route::GET('/index', [ParkingSpotController::class, 'index']);
//Route::POST('/destroy', [ParkingSpotController::class, 'destroy']);
Route::GET('/destroy', [ParkingSpotController::class, 'destroy']);


//Route::POST('/availability', [AvailabilityController::class, 'marking']);

Route::POST('/availability', [AvailabilityController::class, 'id_marking']);

//Route::GET('/destroyavailability', [AvailabilityController::class, 'destroy_id']);
//Route::GET('/destroyavailabilityy', [AvailabilityController::class, 'destroy_availability']);
Route::POST('/availabilitydestroy', [AvailabilityController::class, 'dst']);

Route::POST('/availabilitybutton', [AvailabilityController::class, 'availability_button']);
Route::POST('/availabilitybutton1', [AvailabilityController::class, 'availability_button1']);

Route::POST('/pu', [\App\Http\Controllers\API\Parking2UserController::class, 'parking2user']);

Route::GET('/markers', [MarkerController::class, 'index']);
Route::POST('/claimed', [MarkerController::class, 'claimed_update']);

//Route::middleware('auth:api')->get('/user', function (Request $request) {
//    return $request->user();

//});

Route::group([
    'middleware'=>'auth:api'
], function ()
{

    Route::GET('/logout', [UserController::class, 'logout']);
});
