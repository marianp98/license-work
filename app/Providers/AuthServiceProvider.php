<?php

namespace App\Providers;

use Illuminate\Foundation\Support\Providers\AuthServiceProvider as ServiceProvider;
use Illuminate\Support\Facades\Gate;
use Laravel\Passport\AuthCode;
use Laravel\Passport\Client;
use Laravel\Passport\Passport;
use Laravel\Passport\PersonalAccessClient;
use Laravel\Passport\Token;

class AuthServiceProvider extends ServiceProvider
{
    /**
     * The policy mappings for the application.
     *
     * @var array
     */
    protected $policies = [
         'App\Models\Model' => 'App\Policies\ModelPolicy',
    ];

    /**
     * Register any authentication / authorization services.
     *
     * @return void
     */
    public function boot()
    {
        $this->registerPolicies();
        Passport::routes();
        Passport::useTokenModel(Token::class);
        Passport::useClientModel(Client::class);
        Passport::useAuthCodeModel(AuthCode::class);
        Passport::usePersonalAccessClientModel(PersonalAccessClient::class);
        Passport::tokensExpireIn(now()->addMonths(12));
        Passport::refreshTokensExpireIn(now()->addMonths(12));
        Passport::personalAccessTokensExpireIn(now()->addMonths(12));
    }
}
