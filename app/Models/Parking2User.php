<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Parking2User extends Model
{
    use HasFactory;

    protected $fillable = [
        'user_id',
        'parkingspot_id',
        'availability_id',
    ];

    public function id()
    {
        return $this->id();
    }
    public function availability()
    {
        return $this->hasOne('App\Model\Availability', 'availability_id', 'id');
    }
    public function parkingspot()
    {
        return $this->hasOne('App\Model\ParkingSpot', 'parkingspot_id', 'id');
    }
    public function user()
    {
        return $this->hasOne('App\Model\User', 'user_id', 'id');
    }
}
