package app;

/**
 * Clase que representa un canal de transmisión de datos y permite calcular
 * su capacidad máxima según los modelos de Nyquist y Shannon.
 */
public class TransmisionDatos
{

    private double anchoDeBanda;     // en Hz
    private int niveles;             // M
    private double snrDb;            // en decibelios (dB)

    /**
     * Constructor de la clase TransmisionDatos.
     *
     * @param anchoDeBanda Ancho de banda del canal en Hz.
     * @param niveles Número de niveles de señalización (M).
     * @param snrDb Relación señal a ruido en decibelios (dB).
     */
    public TransmisionDatos(double anchoDeBanda, int niveles, double snrDb)
    {
        this.setAnchoDeBanda(anchoDeBanda);
        this.setNiveles(niveles);
        this.setSnrDb(snrDb);
    }

    /**
     * Obtiene el ancho de banda del canal.
     *
     * @return Ancho de banda en Hz.
     */
    public double getAnchoDeBanda()
    {
        return this.anchoDeBanda;
    }
    
    /**
     * Establece el ancho de banda del canal.
     *
     * @param anchoDeBanda Ancho de banda en Hz.
     */
    private void setAnchoDeBanda(double anchoDeBanda)
    {
        this.anchoDeBanda = anchoDeBanda;
    }

    /**
     * Obtiene el número de niveles de señalización (M).
     *
     * @return Número de niveles.
     */
    public int getNiveles()
    {
        return this.niveles;
    }

    /**
     * Establece el número de niveles de señalización (M).
     *
     * @param niveles Número de niveles.
     */
    private void setNiveles(int niveles)
    {
        this.niveles = niveles;
    }

    /**
     * Obtiene la relación señal a ruido en decibelios (dB).
     *
     * @return SNR en dB.
     */
    public double getSnrDb()
    {
        return this.snrDb;
    }

    /**
     * Establece la relación señal a ruido en decibelios (dB).
     *
     * @param snrDb SNR en dB.
     */
    private void setSnrDb(double snrDb)
    {
        this.snrDb = snrDb;
    }

    /**
     * Calcula la capacidad máxima del canal según el criterio de Nyquist.
     *
     * @return Capacidad máxima en bits por segundo (bps).
     */
    public double calcularCapacidadNyquist()
    {
        if (niveles <= 1) return 0; // log2(1) = 0
        return 2 * anchoDeBanda * (Math.log(niveles) / Math.log(2)); // log base 2
    }

    /**
     * Calcula la capacidad máxima del canal según el criterio de Shannon.
     *
     * @return Capacidad máxima en bits por segundo (bps).
     */
    public double calcularCapacidadShannon()
    {
        double snrLineal = Math.pow(10, snrDb / 10.0); // Conversión de dB a lineal
        return anchoDeBanda * (Math.log(1 + snrLineal) / Math.log(2)); // log base 2
    }
}
