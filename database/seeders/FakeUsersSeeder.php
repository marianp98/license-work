<?php


namespace Database\Seeders;


use App\Models\User;

class FakeUsersSeeder extends \Illuminate\Database\Seeder
{
public function run()
{
   // factory(User::class,10)->create();

    $faker=\Faker\Factory::create();
    for($i=0;$i<5;$i++)
    {
           (new \App\Models\User)->create([
            'name'=>$faker->name,
            'email'=>$faker->email,
            'username'=>$faker->username,
            'password'=>$faker->password,

        ]);
    }
}
}
