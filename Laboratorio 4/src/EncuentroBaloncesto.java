public class EncuentroBaloncesto extends Encuentro {
    public EncuentroBaloncesto(Equipo local, Equipo visitante) {
        super(local, visitante);
    }

    @Override
    protected void validarMarcador(int a, int b) {
        if (a == b) {
            throw new IllegalArgumentException("El baloncesto no permite marcadores empatados.");
        }
    }

    @Override
    protected int calcularPuntos(int propios, int ajenos) {
        return propios > ajenos ? 2 : 1;
    }

    @Override
    protected String nombreDeporte() { return "Baloncesto"; }
}
