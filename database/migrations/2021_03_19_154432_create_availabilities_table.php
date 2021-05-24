<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateAvailabilitiesTable extends Migration
{
    /**
     * Run the migrations.
     * @return void
     */
    public function up()
    {
        Schema::create('availabilities', function (Blueprint $table) {
            $table->id();
            $table->bigInteger('parkingspot_id')->unsigned()->index()->nullable();
            $table->dateTime('start_date');
            $table->dateTime('stop_date');
            $table->timestamps();
            $table->foreign('parkingspot_id')->references('id')->on('parking_spots')->onDelete('cascade')->onUpdate('cascade');
        });
    }
    /**
     * Reverse the migrations.
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('availabilities');
    }
}
