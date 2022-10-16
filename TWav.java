public class TWav extends TArquivoMidia
{
  public TWav ()
  {
    super();
  }

  public TWav (String nomeArquivo)
  {
    super(nomeArquivo);
  }

  public void obtemDadoMidia()
  {
    byte[] conteudo = getConteudo();
    byte[] tamDado = {0, 0, 0, 0, conteudo[43], conteudo[42], conteudo[41], conteudo[40] };
    
    VPPosIniDadosMidia = 44;
    VPTamanhoMidia = TArquivo.bytesToLong(tamDado);
    VPPosFimDadosMidia = VPPosIniDadosMidia+VPTamanhoMidia;
    VPDadosMidia = new byte[(int)VPTamanhoMidia];
    System.arraycopy(conteudo, (int)(VPPosIniDadosMidia), VPDadosMidia, 0, (int)VPTamanhoMidia);
  }
}