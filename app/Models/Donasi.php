<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Donasi extends Model
{
    use HasFactory;

    /**
    * fillable
    *
    * @var array
    */

    protected $fillable = [
        'judulDonasi',
        'Deskripsi',
        'target',
        'namaPenggalang',
        'caraPembayaran',
        'daerah'
    ];
}
