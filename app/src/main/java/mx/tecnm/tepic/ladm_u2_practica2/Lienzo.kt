package mx.tecnm.tepic.ladm_u2_practica2

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.CountDownTimer
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity

val nMoscas  = (80..100).random().toInt()
class Lienzo(p: MainActivity): View(p){
    var principal = p
    var countDownTimer: CountDownTimer? = object : CountDownTimer(60000, 5) {
        override fun onTick(millisUntilFinished: Long) {
            tiempo = "${millisUntilFinished / 1000} Seg."
            if(puntos== nMoscas){
                resultado("¡Ganaste!")
                this.cancel()    }
            if(millisUntilFinished/1000L==0.toLong()){
                resultado("Se Acabo,intentalo de nuevo")}
        }
        override fun onFinish() {
            this.cancel()
        }
    }

    var puntos = 0
    var tiempo =""

    var moscas : Array<Mosca> = Array(nMoscas) { Mosca(this) }


    init{
        countDownTimer?.start()
        val hilo = Hilo(this)
        hilo.start()
    }

    override fun onDraw(c: Canvas){
        super.onDraw(c)
        val paint = Paint()

        c.drawColor(Color.WHITE)
        paint.setColor(Color.BLACK)

        (0 until nMoscas).forEach {
            if(moscas[it].vida>0)
                moscas[it].pintar(c, paint)
        }

        paint.textSize = 70f
        paint.setColor(Color.BLACK)
        c.drawText("Puntuacion: " +puntos.toString(), 80f, 100f, paint)

        paint.textSize = 70f
        c.drawText("Tiempo Restante: "+ tiempo, 80f, 200f, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when(event.action){

            MotionEvent.ACTION_DOWN -> {
                var puntero = 0

                while (puntero <= nMoscas-1) {

                    if (moscas[puntero].hitbox(event.x, event.y)) {
                        if (moscas[puntero].vivo) {
                            puntos++
                            val muerte = BitmapFactory.decodeResource(this.resources, R.drawable.manchar)
                            moscas[puntero].mosIzq = muerte
                            moscas[puntero].mosDer = muerte
                            moscas[puntero].vida = 3
                            moscas[puntero].vivo = false
                        }
                    }
                    puntero++
                }
            }
        }
        invalidate()
        return true
    }

    fun movimiento(){
        (0 until nMoscas).forEach {
            if(moscas[it].vivo)
                moscas[it].moverse(width, height)
                moscas[it].vida = moscas[it].vida - 1
        }
        invalidate()
    }

    fun resultado(m: String){
        AlertDialog.Builder(this.context)
            .setTitle(m)
            .setMessage("Moscas Aplastadas: $puntos")
            .setPositiveButton("Reiniciar"){p, i ->
                run {
                    val intent = Intent(principal,MainActivity::class.java);
                    startActivity(principal,intent,null)
                    principal.finish()
                }
            }
            .show()
    }

}

class Hilo(var p: Lienzo): Thread(){
    override fun run(){
        super.run()

        while(true){
            sleep(500)
            p.principal.runOnUiThread {
                p.movimiento()
            }
        }
    }
}