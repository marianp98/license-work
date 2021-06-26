<?php

namespace App\Http\Controllers\API;

use App\Http\Controllers\Controller;
use App\Http\Resources\UserResource;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;


class UserController extends Controller
{
//    public function __construct()
//    {
//        $this->middleware(function ($request, $next) {
//            $this->user = Auth::user();
//            return $next($request);
//        });
//    }


    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|string',
            'password' => 'required|string'
        ]);
        $credentials = request(['email', 'password']);
        if (!Auth::attempt($credentials)) {
            return response()->json([
                'message' => 'Invalid email or password'
            ], 401);
        }
        $user = $request->user();
        $token = $user->createToken('Access Token');
        $user->access_token = $token->accessToken;
        return $user;
    }

    public function register(Request $request)
    {
        $request->validate([
            'name' => 'required|string',
            'username' => 'required|string|unique:users',
            'phone' => 'required|min:8|max:11',
            'email' => 'required|string|email|unique:users',
            'password' => 'required|string',
            //  'c_password' => 'required|same:password'
        ]);
        $user = new User([
            'name' => $request->name,
            'username' => $request->username,
            'phone' => $request->phone,
            'email' => $request->email,
            'password' => bcrypt($request->password),
        ]);
        $user->save();
        return response()->json([
            "message" => "User registered successfully"
        ], 201);
    }

    public function logout(Request $request)
    {
        $request->user()->token()->revoke();
        return response()->json(
            ['message' => 'User logged out successfully'], 200);
    }

    public function destroyUser(User $user)
    {
        $id = auth('api')->user()->id;
        DB::table('users')->where('id', $id)->delete();
        $user->token()->revoke();

        return response()->json(
            ['message' => 'Account successfully deleted'], 200);
    }

    public function updatePhone(Request $request)
    {
        $request->validate([
            'phone' => 'required|min:8|max:11']);

        $id = auth('api')->user()->id;
        $result = User::find($id);
        $result->phone = $request->phone;
        $result->save();
        return response()->json([
            "phone" => $result['phone']
        ], 200);
    }

    public function updateEmail(Request $request)
    {
        $request->validate([
            'email' => 'required|string|email|unique:users']);
        $id = auth('api')->user()->id;
        $result = User::find($id);
        $result->email = $request->email;
        $result->save();
        return $result;
    }

    public function show(Request $request)
    {
        $id = auth('api')->user()->id;

        $con = mysqli_connect("localhost", "root", "", "parkingdb");
        $sql =
            "SELECT  users.name, users.email, users.username, users.phone
            FROM users
            WHERE users.id='$id'
            GROUP BY users.name,users.email,users.username, users.phone
            ";
        $result = mysqli_query($con, $sql);
        $row = mysqli_fetch_array($result);
        //dd($row);

        $final_result = [
            "id" => $id,
            "name" => $row['name'],
            "email" => $row["email"],
            "username" => $row['username'],
            "phone" => $row['phone']
        ];
        return $final_result;
    }
}
