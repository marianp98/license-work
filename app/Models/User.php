<?php

namespace App\Models;
use Laravel\Passport\HasApiTokens;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;

class User extends Authenticatable
{
    use HasFactory, Notifiable, HasApiTokens;

    protected $table = 'users';
    protected $fillable = [
        'name',
        'username',
        'phone',
        'email',
        'password',
    ];

    protected $primaryKey = 'id';

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function parking() {
        return $this->hasMany(ParkingSpot::class);
    }

    public function access_token() {
        return $this->accessToken;
    }

    public function create(array $data)
    {
        return User::create([

            'name' => $data['name'],
            'username' => $data['username'],
            'phone' => $data['phone'],
            'email' => $data['email'],
            'password' => bcrypt($data['password']),
        ]);
    }
}
