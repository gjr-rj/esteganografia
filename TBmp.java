public class TBmp extends TArquivoMidia
{
  public TBmp ()
  {
    super();
  }
  
  public TBmp (String nomeArquivo)
  {
    super(nomeArquivo);
  }  
   
  void obtemDadoMidia()
  {
    byte[] conteudo = getConteudo();
    byte[] iniDado = {0, 0, 0, 0, conteudo[13], conteudo[12], conteudo[11], conteudo[10] };
    byte[] tamDado = {0, 0, 0, 0, conteudo[37], conteudo[36], conteudo[35], conteudo[34] };
    VPPosIniDadosMidia = TArquivo.bytesToLong(iniDado);
    VPTamanhoMidia = TArquivo.bytesToLong(tamDado);
    VPPosFimDadosMidia = VPPosIniDadosMidia+VPTamanhoMidia;
    VPDadosMidia = new byte[(int)VPTamanhoMidia];
    System.arraycopy(conteudo, (int)(VPPosIniDadosMidia), VPDadosMidia, 0, (int)VPTamanhoMidia);
  }                
}