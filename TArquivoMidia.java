abstract class TArquivoMidia extends TArquivo
{
  protected byte[] VPDadosMidia;
  protected long VPTamanhoMidia;
  protected long VPPosIniDadosMidia;
  protected long VPPosFimDadosMidia;
  
  public TArquivoMidia ()
  {
    super();
  }
  
  public TArquivoMidia (String nomeArquivo)
  {
    super(nomeArquivo);
    obtemDadoMidia();
  }   
  
  public long getTamanhoMidia ()
  {
    return VPTamanhoMidia;
  }
  
  public byte[] getDadosMidia()
  {
    return VPDadosMidia;
  }
  
  public void setDadoMidia (byte[] dadoMidia)
  {
    if (VPDadosMidia.length==dadoMidia.length) VPDadosMidia = dadoMidia;
  }
  
  public void altDadoMidiaArq()
  {
    byte[] conteudoArq = getConteudo();
    System.arraycopy(VPDadosMidia, 0, conteudoArq, (int)VPPosIniDadosMidia, (int)VPTamanhoMidia);
    setConteudo(conteudoArq);
  }
    
  abstract void obtemDadoMidia();
}