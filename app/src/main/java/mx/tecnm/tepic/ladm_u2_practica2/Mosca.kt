package mx.tecnm.tepic.ladm_u2_practica2

import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint

var sentido = true

class Mosca(punteroLienzo: Lienzo) {
    var x = (0..800).random().toFloat()
    var y = (0..1500).random().toFloat()
    private var velocidadx = (40..70).random().toInt()
    private var velocidady = (40..70).random().toInt()
    var mosIzq = BitmapFactory.decodeResource(punteroLienzo.resources, R.drawable.moscad)
    var mosDer = BitmapFactory.decodeResource(punteroLienzo.resources, R.drawable.moscai)
    var vida = 200

    var vivo = true

    fun hitbox(tX:Float,tY:Float): Boolean {
        var x2 = x + mosIzq.width
        var y2 = y + mosIzq.height

        if(tX in x..x2){
            if(tY in y..y2){
                return true
            }
        }
        return false
    }

    fun moverse(ancho:Int, alto:Int){
        x+=velocidadx
        y+=velocidady
        if(x<= -39||x>=ancho){
            velocidadx*=-1
            sentido=!sentido
        }

        else if(y<=-39||y>=alto){
            velocidady*=-1
        }
    }

    fun pintar(c: Canvas, p: Paint){
        if(sentido) {
            c.drawBitmap(mosIzq, x, y, p)
        } else {
            c.drawBitmap(mosDer, x, y, p)
        }
    }
}