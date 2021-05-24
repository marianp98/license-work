<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class ParkingRequests extends FormRequest
{
    public function rules(): array
    {
        return [
            'town' => 'required',
            'parkingNumber' => 'required|integer',
            'address' => 'required',
            'latitude' => 'required|integer',
            'longitude' => 'required|integer',
        ];
    }
}
