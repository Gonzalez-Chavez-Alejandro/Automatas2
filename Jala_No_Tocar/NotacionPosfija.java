package Jala_No_Tocar;

import java.util.LinkedList;


public class NotacionPosfija
{
	private LinkedList<DatosIDS> datos = new LinkedList<DatosIDS>();
	private LinkedList<String> entrada = new LinkedList<String>();
	private LinkedList<String> validacion = new LinkedList<String>();
	private LinkedList<String> orden = new LinkedList<String>();
	private String[]
	prioridad = new String[] {"+-","*/","()"};	
	private String
	agrup="()",
	expNum=("-?[0-9][0-9]*(.[0-9]*[1-9])?"),
	expId=("[a-z]([a-z]|[A-Z])*[0-9]*"),
	expInt=("-?[0-9][0-9]*"),
	expFlo=("-?[0-9][0-9]*(.[0-9]*[1-9])?"),
	expchar=("'([a-z]|[A-Z])'"),
	expcad=("~([a-z]|[A-Z]|[0-9])*~");
	private String op="";
	
	
	private int x=0;
	private int digd,digi;
	
	private int[][]
			asig =  new int[][] //este devuelve valores de true=1 y false=0
			{
				/*int*/		{1,0,1,0},
				/*float*/	{1,1,1,0},
				/*char*/	{1,0,1,0},
				/*String*/	{0,0,0,1},
			},
			suma =  new int[][]//devuelve valores segun el tipo de dato
			{
				/*int*/		{0,1,0,-1},
				/*float*/	{1,1,1,-1},
				/*char*/	{0,1,0,-1},
				/*String*/	{-1,-1,-1,3},
			},
			resta =  new int[][] //devuelve valores segun el tipo de dato
			{
				/*int*/		{0,1,0,-1},
				/*float*/	{1,1,1,-1},
				/*char*/	{0,1,0,-1},
				/*String*/	{-1,-1,-1,-1},
			},
			multi =  new int[][] //devuelve valores segun el tipo de dato
			{
				/*int*/		{0,1,0,0},
				/*float*/	{1,1,1,0},
				/*char*/	{0,1,0,0},
				/*String*/	{-1,-1,-1,-1},
			},
			div =  new int[][] //devuelve valores segun el tipo de dato
			{
				/*int*/		{1,1,1,0},
				/*float*/	{1,1,1,0},
				/*char*/	{1,1,1,0},
				/*String*/	{-1,-1,-1,-1},
			};

	
	public void Datos( LinkedList<DatosIDS> dat)
	{
		datos = dat;
	}
	
	public void Elemento(String cad)
	{
		entrada.add(cad);
	}
	
	
	
	public boolean Asignacion()
	{
		this.mostrarPilas(entrada);
		this.identificar();
		this.mostrarPilas(validacion);
		validacion.addFirst("$");
		
		while(!validacion.getLast().equals("$"))
		{
			digd = Integer.parseInt(validacion.removeLast());
			if(digd==-1)
				return true;
			op = validacion.removeLast();
			if(op=="$")
				break;
			digi = Integer.parseInt(validacion.removeLast());
			validacion.addLast(this.Seleccionar()+"");
		}
		//entrada.clear();
		//validacion.clear();
		if(digd==0)
			return true;
		else
			return false;
	}
	
	public LinkedList<String> orden()
	{
		System.out.println("Orden");
		LinkedList<String> tempSimb = new LinkedList<String>();
		orden.clear();
		int pA,pN,x;
		entrada.add("$");
		orden.add(entrada.removeFirst());
		orden.add(entrada.removeFirst());
		//this.mostrarPilas(orden);
		//this.mostrarPilas(entrada);
		
		while(!entrada.getFirst().equals("$"))
		{
			if(entrada.getFirst().matches(expNum) || entrada.getFirst().matches(expId))
			{
				orden.add(entrada.removeFirst());
			}
			else
			{
				if(!tempSimb.isEmpty())
				{
					if(!entrada.getFirst().equals(")"))
					{
						for(x=0;x<prioridad.length && !prioridad[x].contains(tempSimb.getFirst());x++);
						pA = x; 
						for(x=0;x<prioridad.length && !prioridad[x].contains(entrada.getFirst());x++);
						pN = x; 
						
						if(pA<pN)
						{
							tempSimb.addFirst(entrada.removeFirst());
						}
						else
						{
							if(pA==2)
							{
								tempSimb.addFirst(entrada.removeFirst());
							}
							else
							{
								orden.add(tempSimb.removeFirst());
								tempSimb.addFirst(entrada.removeFirst());
							}	
						}
					}
					else
					{
						while(!tempSimb.getFirst().equals("("))
						{
							orden.add(tempSimb.removeFirst());
						}
						tempSimb.removeFirst();
						entrada.removeFirst();
					}
				}
				else
					tempSimb.addFirst(entrada.removeFirst());
				
			}
		}	
		entrada.clear();
		validacion.clear();
		orden.addAll(tempSimb);
		this.mostrarPilas(orden);
		return orden;
	}
	
	private void identificar()
	{
		int y;
		for(x=0;x<entrada.size();x++)
		{
			if(entrada.get(x).matches(expId))
			{
				
				for(y=0;y<datos.size() && !datos.get(y).getId().equals(entrada.get(x));y++);
				if(y<datos.size())
					validacion.add(datos.get(y).getTip());
				else
					if(entrada.get(x).equals("read"))
					{
						validacion.add(validacion.getFirst());
						break;
					}
			}
			else
				if(entrada.get(x).matches(expInt))
				{
					validacion.add("0");
				}
				else
					if(entrada.get(x).matches(expFlo))
					{
						validacion.add("1");
					}
					else
						if(entrada.get(x).matches(expchar))
						{
							validacion.add("2");
						}
						else
							if(entrada.get(x).matches(expcad))
							{
								validacion.add("3");
							}
							else
								if(!agrup.contains(entrada.get(x)))
									validacion.add(entrada.get(x));
									
		}
	}
	
	private int Seleccionar()
	{
		int res=-2;
		switch(op)
		{
			case "+" :
				res = suma[digi][digd];
				break;
				
			case "-" :
				res = resta[digi][digd];
				break;
				
			case "*":
				res = multi[digi][digd];
				break;
			
			case "/":
				res = div[digi][digd];
				break;
				
			case "=":
				res = asig[digi][digd];
		}
		return res;
	}
	
	private void mostrarPilas(LinkedList<String> pila)
	{
		System.out.println("ValidacionPrio---Mostrar-->");
		for(int x = 0; x<pila.size();x++)
			System.out.println(pila.get(x));
	}
}
