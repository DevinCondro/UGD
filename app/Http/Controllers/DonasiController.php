<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Validator;
use App\Models\Donasi;

class DonasiController extends Controller
{
    public function index()
    {
        $donasis = Donasi::all();
        if(count($donasis) > 0){
            return response([
                'message' => 'Retrieve All Success',
                'data' => $donasis
            ],200);
        }

        return response([
            'message' => 'Empty',
            'data' => null
        ],400); 
    }

    public function show($id)
    {
        $donasis = Donasi::find($id);
        if(!is_null($donasis)){
            return response([
                'message' =>'Retrieve Donasi Success',
                'data' => $donasis
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
            'judulDonasi' => 'required',
            'Deskripsi' => 'required',
            'target' => 'required',
            'namaPenggalang' => 'required',
            'caraPembayaran' => 'required',
            'daerah' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);
            
        $user = Donasi::create($storeData);

        return response([
            'message' => 'Add Pengguna Success',
            'data' => $user
        ], 200);
    }

    public function update(Request $request, $id)
    {
        $userData = Donasi::find($id);

        if(is_null($userData)) {
            return response([
                'message' => 'User Not Found',
                'data' => null
            ], 404);
        }

        $updateDataUser  = $request->all();
        $validate = Validator::make($updateDataUser, [
            'judulDonasi' => 'required',
            'Deskripsi' => 'required',
            'target' => 'required',
            'namaPenggalang' => 'required',
            'caraPembayaran' => 'required',
            'daerah' => 'required'
        ]);

        if($validate->fails()) {
            return response(['message' => $validate->errors()], 400);
        }

        $userData->judulDonasi = $updateDataUser['judulDonasi'];
        $userData->Deskripsi = $updateDataUser['Deskripsi'];
        $userData->target = $updateDataUser['target'];
        $userData->namaPenggalang = $updateDataUser['namaPenggalang'];
        $userData->caraPembayaran = $updateDataUser['caraPembayaran'];
        $userData->daerah = $updateDataUser['daerah'];

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
        $donasis = Donasi::find($id);
        if(is_null($donasis)){
            return response([
                'message' =>'Pengguna Not Found',
                'data' => null
            ], 404);
        }

        if($donasis->delete()){
            return response([
                'message' =>'Delete Employee Success',
                'data' => $donasis
            ], 200);
        }

        return response([
            'message' =>'Delete Pengguna Failed',
            'data' => null
        ], 400);

    }
}
