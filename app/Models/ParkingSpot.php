<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
use Laravel\Passport\HasApiTokens;

class ParkingSpot extends Model
{
    use HasFactory, Notifiable, HasApiTokens;

    protected $fillable = [
        'user_id',
        'town',
        'parkingNumber',
        'address',
        'latitude',
        'longitude',
        'claimed'
    ];

    protected $primaryKey = 'id';
    public function id()
    {
        return $this->id();
    }

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

    public function availability()
    {
        return $this->hasOne('App\Model\Availability', 'parkingspot_id', 'id');
    }

    public function user() {
        return $this->belongsTo(User::class, 'user_id');
    }

}
