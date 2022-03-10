import java.net.Socket

var SLEEP1:Long = 1000*10
var SLEEP2:Long = 1000*60*3-SLEEP1
var HOST = "8.8.8.8"
var PORT = 53

fun checkInternet(): Boolean {
    var internet = false
    try {
        val socket = Socket(HOST, PORT)
        if(socket.isConnected)
            internet = true
        socket.close()
    }
    catch(e: Exception){}

    //println(if(internet) "Connected" else "Disconnected")
    return internet
}

fun exec(cmd:String) {
    try{
        Runtime.getRuntime().exec(cmd)
    } catch (e: Exception){}
}

fun main(args: Array<String>) {
    if(args.size != 4)
        println("java -jar InternetConnectionReboot.jar [HOST] [PORT] [SLEEP1] [SLEEP2]")
    else
    {
        HOST = args[0]
        PORT = args[1].toInt()
        SLEEP1 = args[2].toLong()*1000
        SLEEP2 = args[3].toLong()*1000
    }

    while(true) {
        while(checkInternet())
            Thread.sleep(SLEEP1)

        Thread.sleep(SLEEP2)
        if(!checkInternet())
            try {
                exec("/usr/sbin/reboot")
                exec("/usr/sbin/shutdown -r now")
                exec("reboot")
                exec("shutdown -r now")
                exec("systemctl --no-wall reboot")
            } catch (e: Exception) {}
    }
}