<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
class Availability extends Model
{
    use HasFactory;

    protected $fillable = [
        'parkingspot_id',
        'start_date',
        'stop_date',
    ];

    protected $primaryKey = 'id';

    /**
     * The attributes that should be hidden for arrays.
     * @var array
     */
//    protected $hidden = [
//        'password',
//        'remember_token',
//    ];

    /**
     * The attributes that should be cast to native types.
     * @var array
     */
    protected $casts = [
        'address_verified_at' => 'datetime',
    ];

    public function parking_spots()
    {
        return $this->belongsTo(ParkingSpot::class,'parkingspot_id');
    }
}
