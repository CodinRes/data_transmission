package app;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;

/**
 * Interfaz gráfica para el cálculo y visualización de la capacidad de un canal de transmisión de datos
 * según los modelos de Nyquist y Shannon.
 * <p>
 * Permite al usuario ingresar el ancho de banda, el número de niveles y la relación señal a ruido (SNR),
 * mostrando los resultados y un gráfico comparativo.
 */
public class InterfazTransmisionDatos extends JFrame
{

    private JTextField campoBanda;
    private JTextField  campoNiveles;
    private JTextField  campoSnr;
    private JButton botonCalcular;
    private JLabel resultadoNyquist;
    private JLabel  resultadoShannon;
    private ChartPanel chartPanel;

    /**
     * Obtiene el campo de texto para el ancho de banda.
     * @return JTextField correspondiente al ancho de banda.
     */
    public JTextField getCampoBanda()
    {
        return this.campoBanda;
    }

    /**
     * Obtiene el campo de texto para el número de niveles.
     * @return JTextField correspondiente a los niveles.
     */
    public JTextField getCampoNiveles()
    {
        return this.campoNiveles;
    }

    /**
     * Obtiene el campo de texto para la relación señal a ruido (SNR).
     * @return JTextField correspondiente al SNR.
     */
    public JTextField getCampoSnr()
    {
        return this.campoSnr;
    }

    /**
     * Obtiene el botón para calcular.
     * @return JButton de cálculo.
     */
    public JButton getBotonCalcular()
    {
        return this.botonCalcular;
    }

    /**
     * Obtiene la etiqueta de resultado para Nyquist.
     * @return JLabel del resultado Nyquist.
     */
    public JLabel getResultadoNyquist()
    {
        return this.resultadoNyquist;
    }

    /**
     * Obtiene la etiqueta de resultado para Shannon.
     * @return JLabel del resultado Shannon.
     */
    public JLabel getResultadoShannon()
    {
        return this.resultadoShannon;
    }

    /**
     * Obtiene el panel del gráfico.
     * @return ChartPanel del gráfico.
     */
    public ChartPanel getChartPanel()
    {
        return this.chartPanel;
    }

    // Setters privados para encapsulamiento

    private void setCampoBanda(JTextField campoBanda)
    {
        this.campoBanda = campoBanda;
    }

    private void setCampoNiveles(JTextField campoNiveles)
    {
        this.campoNiveles = campoNiveles;
    }

    private void setCampoSnr(JTextField campoSnr)
    {
        this.campoSnr = campoSnr;
    }

    private void setBotonCalcular(JButton botonCalcular)
    {
        this.botonCalcular = botonCalcular;
    }

    private void setResultadoNyquist(JLabel resultadoNyquist)
    {
        this.resultadoNyquist = resultadoNyquist;
    }

    private void setResultadoShannon(JLabel resultadoShannon)
    {
        this.resultadoShannon = resultadoShannon;
    }

    private void setChartPanel(ChartPanel chartPanel)
    {
        this.chartPanel = chartPanel;
    }

    /**
     * Constructor de la interfaz gráfica.
     * Inicializa los componentes, organiza el layout y define los listeners.
     */
    public InterfazTransmisionDatos()
    {
        this.setTitle("Transmisión de Datos");
        this.setSize(1920, 1080);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Panel superior: controles
        JPanel panelControles = new JPanel(new GridLayout(2, 4, 10, 5));

        this.setCampoBanda(new JTextField("3000"));    // Hz
        this.setCampoNiveles(new JTextField("4"));
        this.setCampoSnr(new JTextField("30"));        // dB

        panelControles.add(new JLabel("Ancho de Banda (Hz):"));
        panelControles.add(this.getCampoBanda());
        panelControles.add(new JLabel("Niveles (M):"));
        panelControles.add(this.getCampoNiveles());
        panelControles.add(new JLabel("SNR (dB):"));
        panelControles.add(this.getCampoSnr());

        this.setBotonCalcular(new JButton("Calcular"));
        panelControles.add(this.getBotonCalcular());

        add(panelControles, BorderLayout.NORTH);

        // Resultados
        JPanel panelResultados = new JPanel(new GridLayout(2, 1));
        this.setResultadoNyquist(new JLabel("Capacidad Nyquist: "));
        this.setResultadoShannon(new JLabel("Capacidad Shannon: "));
        panelResultados.add(this.getResultadoNyquist());
        panelResultados.add(this.getResultadoShannon());
        add(panelResultados, BorderLayout.SOUTH);

        // Panel gráfico
        this.setChartPanel(new ChartPanel(null));
        add(this.getChartPanel(), BorderLayout.CENTER);

        this.getBotonCalcular().addActionListener(e -> calcularYGraficar());

        this.setVisible(true);
    }

    /**
     * Realiza el cálculo de las capacidades de canal según Nyquist y Shannon,
     * actualiza los resultados y genera el gráfico comparativo.
     */
    private void calcularYGraficar()
    {
        try
        {
            double B = Double.parseDouble(this.getCampoBanda().getText());
            int M = Integer.parseInt(this.getCampoNiveles().getText());
            double snrDb = Double.parseDouble(this.getCampoSnr().getText());

            TransmisionDatos canal = new TransmisionDatos(B, M, snrDb);

            double nyquist = canal.calcularCapacidadNyquist();
            double shannon = canal.calcularCapacidadShannon();

            this.getResultadoNyquist().setText(String.format("Capacidad Nyquist: %.2f bps", nyquist));
            this.getResultadoShannon().setText(String.format("Capacidad Shannon: %.2f bps", shannon));

            // Gráfico comparativo con mismo ancho de banda
            XYSeries serieNyquist = new XYSeries("Nyquist (M variable)");
            for (int niveles = 2; niveles <= 32; niveles += 2)
            {
                TransmisionDatos td = new TransmisionDatos(B, niveles, snrDb);
                serieNyquist.add(niveles, td.calcularCapacidadNyquist());
            }

            XYSeries puntoNyquist = new XYSeries("Nyquist (actual)");
            puntoNyquist.add(M, nyquist);

            XYSeries serieShannon = new XYSeries("Shannon (SNR variable)");
            for (int snrTest = 0; snrTest <= 60; snrTest += 5)
            {
                TransmisionDatos td = new TransmisionDatos(B, M, snrTest);
                serieShannon.add(snrTest, td.calcularCapacidadShannon());
            }

            XYSeries puntoShannon = new XYSeries("Shannon (actual)");
            puntoShannon.add(snrDb, shannon);

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(serieNyquist);
            dataset.addSeries(puntoNyquist);
            dataset.addSeries(serieShannon);
            dataset.addSeries(puntoShannon);

            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Capacidad de Canal",
                    "M (Niveles) o SNR (dB)",
                    "Capacidad (bps)",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );

            this.getChartPanel().setChart(chart);

        } catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Ingresá valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
