<?php

namespace Database\Seeders;

use \App\Models\User;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        $users = [
            [
                'name' => 'Test',
                'username' => 'user',
                'email' => 'admin@domain.tld',
                'password' => Hash::make('Test1234'),
            ]
        ];

        foreach ($users as $user) {
            if (!User::where('email', $user['email'])->exists()) {
                User::create($user);
            }
        }

         //\App\Models\User::factory(10)->create();
    }
}
