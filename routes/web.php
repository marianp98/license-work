<?php

use App\Http\Controllers\API\ParkingSpotController;
use App\Http\Controllers\API\UserController;
use App\Http\Controllers\API\MarkerController;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Route;
use Laravel\Passport\Passport;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/
Passport::routes();

Route::get('/', function () {
    return view('welcome');
});
Route::get('/test', function () {
    return view('test');
});
Route::GET('/register', [UserController::class, 'register']);
Route::GET('/login', [UserController::class, 'login']);
Route::GET('/parking', [ParkingSpotController::class, 'parking']);
Route::GET('/markers', [MarkerController::class, 'index']);



Auth::routes();

Route::get('/home', [App\Http\Controllers\HomeController::class, 'index'])->name('home');
