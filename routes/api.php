<?php

use App\Http\Controllers\API\AvailabilityController;
use App\Http\Controllers\API\ParkingSpotController;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\MarkerController;
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


// ********************USER related*********************
Route::GET('/register', [UserController::class, 'register']);
Route::POST('/login', [UserController::class, 'login']);
Route::POST('/destroyuser', [UserController::class, 'destroyUser']);
Route::GET('/showuser', [UserController::class, 'show']);
Route::POST('/updateemail', [UserController::class, 'updateEmail']);
Route::GET('/updatephone', [UserController::class, 'updatePhone']);
//Route::GET('/token', [UserController::class, 'get_token']);
//Route::POST('/updateuser', [UserController::class, 'updateUser']);
//Route::resource('user', UserController::class)->except('create', 'edit');


// ********************ParkingSpot related*********************
Route::POST('/parking', [ParkingSpotController::class, 'parking']);
Route::GET('/updateparking', [ParkingSpotController::class, 'updateParkingSpot']); //Parking Number edit, in future updates
Route::GET('/showparking', [ParkingSpotController::class, 'show']);
Route::GET('/destroy', [ParkingSpotController::class, 'destroy']);
Route::POST('/attachDate', [ParkingSpotController::class, 'updateParkingSpotDate']);


//******************************Availability related*************************
Route::POST('/availabilitydestroy', [AvailabilityController::class, 'dst']);
Route::POST('/availability', [AvailabilityController::class, 'id_marking']);
//Route::POST('/availabilitybutton', [AvailabilityController::class, 'availability_button']); unknown
//Route::POST('/showAvailableSpots', [AvailabilityController::class, 'show_available_spots']);  // for update
//Route::POST('/showUnavailableSpots', [AvailabilityController::class, 'show_unavailable_spots']); //for update


//****************************Marker related*******************************
Route::GET('/markers', [MarkerController::class, 'index']);
Route::POST('/claimed', [MarkerController::class, 'claimed_update']);
Route::POST('/available', [MarkerController::class, 'available_update']);


Route::group([
    'middleware' => 'auth:api'
], function () {
    Route::GET('/logout', [UserController::class, 'logout']);

});
