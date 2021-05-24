<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ParkingResource extends JsonResource
{
    /**
     * @param Request $request
     * @return array
     * @SuppressWarnings(PHPMD.UnusedFormalParameter)
     */
    public function toArray($request): array
    {
        return [
            'id' => $this->id,
            'user_id' => $this->user_id,
            'user_data' => new UserResource($this->user),
            'town' => $this->town,
            'parkingNumber' => $this->parkingNumber,
            'address' => $this->address,
            'latitude' => $this->latitude,
            'longitude' => $this->longitude,
            'created_at' => optional($this->created_at)->format('Y-m-d H:i:s'),
            'updated_at' => optional($this->updated_at)->format('Y-m-d H:i:s'),
        ];
    }
}
