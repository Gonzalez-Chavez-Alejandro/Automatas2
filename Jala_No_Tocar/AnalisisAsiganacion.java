package Jala_No_Tocar;

import java.util.LinkedList;

import lib20.Datos;

public class AnalisisAsiganacion
{
	
	private Datos obd = new Datos();
	private String op="",
			Tipos[] = new String[] {"int","float","char"},
			numEnt=("-?[0-9][0-9]*"),
			numFloat=("-?[0-9][0-9]*(.[0-9]*[1-9])");
			
			
	private int digd,digi;
	private LinkedList<String> entrada = new LinkedList<String>();//entrada Principal
	private LinkedList<String> convertidos = new LinkedList<String>();//entrada modificada con valores de la prioridad del operador
	
	
	private int[][]
			asig =  new int[][] //este devuelve valores de true=1 y false=0
			{
				{1,1,1},
				{1,1,1},
				{1,0,1},
			},
			suma =  new int[][]//devuelve valores segun el tipo de dato
			{
				{0,1,0},
				{1,1,1},
				{0,1,0},
			},
			resta =  new int[][] //devuelve valores segun el tipo de dato
			{
				{0,1,0},
				{1,1,1},
				{0,1,0},
			},
			multi =  new int[][] //devuelve valores segun el tipo de dato
			{
				{0,1,0},
				{1,1,1},
				{0,1,0},
			},
			div =  new int[][] //devuelve valores segun el tipo de dato
			{
				{1,1,1},
				{1,1,1},
				{1,1,1},
			};

	public void Nuevo(String ele)
	{
		entrada.add(ele);
	}
	
	public boolean Analizar()
	{
		this.Convertir();
		convertidos.addFirst("$");
		obd.Println("Convertidos AnalisisAsign---------------------------");
		this.mostrarPilas(convertidos);
		while(!convertidos.getLast().equals("$"))
		{
			digd = Integer.parseInt(convertidos.removeLast());
			op = convertidos.removeLast();
			if(op=="$")
				break;
			digi = Integer.parseInt(convertidos.removeLast());
			convertidos.addLast(this.Seleccionar()+"");
		}
		convertidos.clear();
		if(digd==1)
			return false;
		else
			return true;

	}
	
	private void Convertir()
	{
		int x;
		for(String T:entrada)
		{
			if(!T.equals("(") && !T.equals(")"))
			{
				for(x=0;x<Tipos.length && !Tipos[x].equals(T);x++);
				if(x<Tipos.length)
					convertidos.add(x+"");
				else
					if(T.matches(numEnt))//mandar su tipo de dato
						convertidos.add("0");
					else
						if(T.matches(numFloat))
							convertidos.add("1");
						else
							convertidos.add(T);	
			}
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
	
	public void mostrarPilas(LinkedList<String> pila)
	{
		System.out.println("Mostrar");
		for(int x = 0; x<pila.size();x++)
			System.out.println(pila.get(x));
	}
}
