package com.keskin.minerva.jwt;


import com.keskin.minerva.entities.AppUser;

public interface IJwtService {

    Jwt generateAccessToken(AppUser user);

    Jwt generateRefreshToken(AppUser user);

    Jwt parseToken(String token);

}
