<?php

namespace App\Http\Controllers;

use App\Models\Pengguna;
use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Validator;

class PenggunaController extends Controller
{
        /**
    * index
    *
    * @return void
    */
    public function index()
    {
        $penggunas = Pengguna::all();
        if(count($penggunas) > 0){
            return response([
                'message' => 'Retrieve All Success',
                'data' => $penggunas
            ],200);
        }

        return response([
            'message' => 'Empty',
            'data' => null
        ],400); 
    }

    public function show($id)
    {
        $penggunas = Pengguna::find($id);
        if(!is_null($penggunas)){
            return response([
                'message' =>'Retrieve Employee Success',
                'data' => $penggunas
            ], 200);
        }
        return response([
            'message' =>'Employee Not Found',
            'data' => null
        ], 400);
    }

    public function store(Request $request)
    {
        $storeData = $request->all();
        $validate = Validator::make($storeData, [
            'username' => 'required',
            'Email' => 'required||email:rfc,dns',
            'nomorTelepon' => 'required',
            'tanggal' => 'required',
            'password' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);
            
        $user = Pengguna::create($storeData);

        return response([
            'message' => 'Add Pengguna Success',
            'data' => $user
        ], 200);
    }

    public function update(Request $request, $id)
    {
        $userData = Pengguna::find($id);

        if(is_null($userData)) {
            return response([
                'message' => 'User Not Found',
                'data' => null
            ], 404);
        }

        $updateDataUser  = $request->all();
        $validate = Validator::make($updateDataUser, [
            'username' => 'required',
            'Email' => 'required||email:rfc,dns',
            'nomorTelepon' => 'required',
            'tanggal' => 'required',
            'password' => 'required'
         //   'image' => 'required|image:jpeg,png,jpg,gif,svg|max:2048'
        ]);

        if($validate->fails()) {
            return response(['message' => $validate->errors()], 400);
        }

        $userData->username = $updateDataUser['username'];
        $userData->Email = $updateDataUser['Email'];
        $userData->nomorTelepon = $updateDataUser['nomorTelepon'];
        $userData->tanggal = $updateDataUser['Tanggal'];
        $userData->password = $updateDataUser['password'];

        if($userData->save()) {
            return response([
                'message' => 'Update Data User Success',
                'data' => $userData
            ], 200);
        }

        return response([
            'message' => 'Update User Failed',
            'data' => null
        ], 400);
    }

    public function destroy($id)
    {
        $penggunas = Pengguna::find($id);
        if(is_null($penggunas)){
            return response([
                'message' =>'Pengguna Not Found',
                'data' => null
            ], 404);
        }

        if($penggunas->delete()){
            return response([
                'message' =>'Delete Employee Success',
                'data' => $penggunas
            ], 200);
        }

        return response([
            'message' =>'Delete Pengguna Failed',
            'data' => null
        ], 400);

    }
}
