package com.example.duc25.config

class UriApi(username: String?, password: String?, id: Int?, onoff: Int?) {
    val uriApiMyhome: String = "https://techitvn.com/home/api/myhome.php?token="
    val uriApiLogin = "https://techitvn.com/home/api/login.php?username=$username&password=$password"
    val uriAPiRemote = "https://techitvn.com/home/api/remote.php?namedevice=light&id=$id&status=$onoff&token="
}