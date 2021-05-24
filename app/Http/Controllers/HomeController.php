<?php

namespace App\Http\Controllers;

use Illuminate\Contracts\Foundation\Application;
use Illuminate\Contracts\View\Factory;
use Illuminate\Contracts\View\View;
use Illuminate\Http\Response;
use Illuminate\Support\Facades\Auth;

class HomeController extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');

        $this->middleware(function($request,$next){
            $this->user=Auth::user(); // here the user should exist from the session
            return $next($request);
    });
    }

    /**
     * Show the application dashboard.
     *
     * @return Application|Factory|View|Response
     */
    public function index()
    {
        return view('home');
    }


}
