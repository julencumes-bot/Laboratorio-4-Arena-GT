public class EncuentroFutbolSala extends Encuentro {
    public EncuentroFutbolSala(Equipo local, Equipo visitante) {
        super(local, visitante);
    }

    @Override
    protected void validarMarcador(int a, int b) {
        // Se permiten empates. Encuentro ya comprobó que no haya negativos.
    }

    @Override
    protected int calcularPuntos(int propios, int ajenos) {
        if (propios > ajenos) { return 3; }
        if (propios == ajenos) { return 1; }
        return 0;
    }

    @Override
    protected String nombreDeporte() { return "Fútbol sala"; }
}
