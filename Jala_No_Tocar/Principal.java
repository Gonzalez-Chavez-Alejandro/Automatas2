package Jala_No_Tocar;

import java.util.LinkedList;

import lib20.Datos;

public class Principal
{
	private DatosIDS dat;
	private Datos obd = new Datos();
	private AnalisisAsiganacion anaAsig = new AnalisisAsiganacion();
	private NotacionPosfija ntPos = new NotacionPosfija();
	private CodigoIntermedio codInter = new CodigoIntermedio();
	
	private int pos=0,contador=0;//posicion de error
	private boolean ban=false;
	
	private String[] 
	terminales = new String[] {"num","id","int","float","char",",",";","+","-","*","/","(",")","="},
			Tipos = new String[] {"int","float","char"},
			operadores = new String[] {"+","-","*","/","=","(",")"},

	
	columnas = new String[]{"num","id", "int", "float", "char", ",", ";", "+", "-", "*", "/", "(", ")",  "=", "$", "P", "Tipo", "V", "A",  "E",  "T", "F"},
            estados = new String[]{"I00", "I01", "I02", "I03", "I04", "I05", "I06", "I07", "I08", "I09", "I10", "I11", "I12", "I13", "I14", "I15", "I16", "I17", "I18", "I19",
                 "I20", "I21", "I22", "I23", "I24", "I25", "I26", "I27", "I28", "I29", "I30", "I31", "I32"},
            
	producciones = new String[]{"Q", "P", "P", "V", "V", "A", "E", "E", "E", "T", "T","T","F","F","F","Tipo","Tipo","Tipo"},
            //0   1   2     3     4      5     6   7   8    9    0   1   2    3     4   5   6   7   8   9    0     1
            noterminales = new String[]{"P", "Tipo", "V", "A", "E", "T", "F"};

	
	private String 
	expId=("[a-z]([a-z]|[A-Z])*[0-9]*"),
	expNum=("-?[0-9][0-9]*(.[0-9]*[1-9])?"),
	elchar=("'([a-z]|[A-Z])'"),
	temp="";//guarda elementos sin importacia repetidos por la tablasintactica
	
	private int[] 
			prodTam = new int [] {1,3,1,3,2,4,3,3,1,3,3,1,3,1,1,1,1,1};
			
	private LinkedList<DatosIDS> datos = new LinkedList<DatosIDS>(); //pila donde se guardan las variables,tipode datos,valor
	private LinkedList<String> entraUser = new LinkedList<String>();//pila de entrada del usuario no modificado
	private LinkedList<String> entrada = new LinkedList<String>();//pila modifica para usar tabla sintactica
	private LinkedList<String> pilaT = new LinkedList<String>();//pila de estados de la tabla sintactica
	
	private String[][] tablaSint = new String[][]{//		id	    int		 float	char	 	,			 ;		+		-		*		 /		(	     )		num	  	=   	$		P		Tipo		V		   A	EXP   	E		TERM  	T		F
                         //	num	 id      int	 float	 char	  ,	 ;	 +	 -	 *	 /	(	 )	=   	$	P	Tipo	V        A	E       T	    F
			/*I00*/	{"",	 "I07",	 "I04",	 "I05",	 "I06",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"I01",	"I02",	"",	"I03",	"",     "",         "" },
			/*I01*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P0",	"",	"",	"",	"",	"",     "",         "" },
			
                        /*I02*/	{"",	 "I08",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I03*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P2",	"",	"",	"",	"",	"",     "",         "" },
			/*I04*/	{"",	 "P15",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I05*/	{"",	 "P16",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I06*/	{"",	 "P17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I07*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"I09",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I08*/	{"",	 "",	 "",	 "",	 "",	 "I11",	 "I12",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"I10",	"",	"",     "",         "" },
			/*I09*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"I13",  "I14",      "I15" },
			/*I10*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P1",	"",	"",	"",	"",	"",     "",         "" },
			/*I11*/	{"",	 "I19",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },			
                        /*I12*/	{"",	 "I07",	 "I04",	 "I05",	 "I06",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"I20",	"I02",	"",	"I03",	"",     "",         "" },
			/*I13*/	{"",	 "",	 "",	 "",	 "",	 "",	 "I21",	 "I22",	 "I23",	 "",	"",	"",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I14*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P8",	 "P8",	 "P8",	 "I24",	"I25",	"",	"P8",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I15*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P11",	 "P11",	 "P11",	 "P11",	"P11",	"",	"P11",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I16*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"I26",  "I14",      "I15" },
			
                     	/*I17*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P13",	 "P13",	 "P13",	 "P13",	"P13",	"",	"P13",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I18*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P14",	 "P14",	 "P14",	 "P14",	"P14",	"",	"P14",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I19*/	{"",	 "",	 "",	 "",	 "",	 "I11",	 "I12",	 "",	 "",	 "",	"",	"",	"",	"",	"",	"",	"",	"I27",	"",	"",     "",         "" },
			/*I20*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P4",	"",	"",	"",	"",	"",     "",         "" },
			/*I21*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P5",	"",	"",	"",	"",	"",     "",         "" },
			/*I22*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"",     "I28",      "I15" },
			/*I23*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"",     "I29",      "I15" },
			/*I24*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "I30" },
			/*I25*/	{"I18",	 "I17",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"I16",	"",	"",	"",	"",	"",	"",	"",	"",     "",         "I31" },
			/*I26*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "I22",	 "I23",	 "",	"",	"",	"I32",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I27*/	{"",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	 "",	"",	"",	"",	"",	"P3",	"",	"",	"",	"",	"",     "",         "" },
			/*I28*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P6",	 "P6",	 "P6",	 "I24",	"I25",	"",	"P6",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I29*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P7",	 "P7",	 "P7",	 "P7",	"P7",	"",	"P7",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I30*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P9",	 "P9",	 "P9",	 "P9",	"P9",	"",	"P9",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I31*/	{"",	 "",	 "",	 "",	 "",	 "",	 "P10",	 "P10",	 "P10",	 "P10",	"P10",	"",	"P10",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			/*I32*/	{"",	 "I08",	 "",	 "",	 "",	 "",	 "P12",	 "P12",	 "P12",	 "P12",	"P12",	"",	"P12",	"",	"",	"",	"",	"",	"",	"",     "",         "" },
			}; 
			
	LinkedList<String> tablaSimbolos=new LinkedList<String>();		
	
	private void Error(int pos,String cad)	
	{
		for(int x=0;x<entrada.size()-1;x++)
		{
			if(x==pos)
				obd.Print("??"+entraUser.get(x)+"??");
			else
				obd.Print(entraUser.get(x)+" ");
		}
		if(entrada.get(pos)=="null0")
		{
			obd.Println("La variable no ha sido declarada");
			
		}else
			if(entrada.get(pos)=="null1")
			{
				obd.Println("La variable se repite");
				
			}
			else
				if(!cad.isBlank())
				{
					obd.Println(cad);
				}
				else
				{
					obd.Println(entrada.get(pos)=="$"?"Se debe continuar la expresion":"Error en el elemento \t ??"+entraUser.get(pos)+"??");
					obd.Print("Valores posibles aceptados: \t");
					int rango = Integer.parseInt(pilaT.getLast().substring(1)),y;
					for(int x=0;x<tablaSint[rango].length;x++)
					{
						if(!tablaSint[rango][x].isBlank())
						{
							for(y=0;y<noterminales.length && !noterminales[y].equals(columnas[x]) ;y++);
							if(y<noterminales.length)
							{
								//obd.Print("Siguientes "+columnas[x]);
							}
							else
								obd.Print(columnas[x]);
							obd.Print("\\");
						}
					}
				}
	}
			
	public void Accion (LinkedList<String>pila)
	{
		pilaT.add("$");
		pilaT.add("I00");
		int x,y,z,cantEliminar;
		
		while(!pila.getFirst().equals("Q"))
		{
			for(x=0;x<estados.length && !pilaT.getLast().equals(estados[x]) ;x++);//////Tabla sintactica
			for(y=0;y<columnas.length && !pila.getFirst().equals(columnas[y]) ;y++);//////Tabla sintactica
			
			if(x<estados.length && y<columnas.length)//////Tabla sintactica
			{
				obd.Println(pilaT.getLast()+"/"+pila.getFirst());//////Tabla sintactica
				
				
				
				System.out.print("///////////////////////////////////");//////Tabla sintactica
				this.mostrarPilas(pilaT);//////Tabla sintactica
				
				if(!tablaSint[x][y].isBlank())//////Tabla sintactica
				{
					if(tablaSint[x][y].charAt(0)=='I')//////Tabla sintactica
					{
						
						pilaT.add(tablaSint[x][y]);//////Tabla sintactica
						pos++;//////Tabla sintactica
						this.eleInter(pila.getFirst());//agrega elementos codigo intermedio
						pila.removeFirst();//////Tabla sintactica
						this.elementosAsignacion(pila.getFirst());//manda los elementos para analizar antes de la asignacion
						//Comprueba q los elemetos si se puedan asignar
						if(this.ComprobarAsigna(pila.getFirst()))//si es error truena
							break;
						
					}else
						if(tablaSint[x][y].charAt(0)=='P')//////Tabla sintactica
						{
							obd.Println("Produccion");
							pos--;
							cantEliminar = Integer.parseInt(tablaSint[x][y].substring(1));//////Tabla sintactica
							for(z=0;z<prodTam[cantEliminar];z++)//////Tabla sintactica
								pilaT.removeLast();//////Tabla sintactica
							if(tablaSint[x][y].equals("P0"))//////Tabla sintactica
							{
								System.out.println("CADENA ACEPTADA->Resultado");//////Tabla sintactica
								codInter.Datos(datos);
								obd.Println("Intermedio code");
								this.mostrarPilas(codInter.Traducir());
								break;//////Tabla sintactica
							}
							pila.addFirst(producciones[cantEliminar]);//////Tabla sintactica
						}
				}
				else//////Tabla sintactica
				{
					this.Error(pos, "");//////Tabla sintactica
					break;//////Tabla sintactica
				}
			}else//////Tabla sintactica
			{
				this.Error(pos, "");//////Tabla sintactica
				break;//////Tabla sintactica
			}
		}
	}
	
	private void elementosAsignacion(String ele)
	{
		int ind;
		
		if((pilaT.getLast()=="I07" && ele.equals("id") || ban))
		{
			ban = true;
			obd.Println("Asignando");
			for(ind=0;ind<operadores.length && !operadores[ind].equals(ele);ind++);
			if(ind<operadores.length)
			{
				if(!temp.equals(ele))
				{
					anaAsig.Nuevo(ele);//mandarlo igual para analisisAgignacion
					ntPos.Elemento(entraUser.get(pos));//mandarlo igual para notacionposfija
					temp = ele;
				}
			}else
			{
				if(ele.equals("id"))
				{
					for(ind=0;ind<datos.size() && !datos.get(ind).getId().equals(entraUser.get(pos));ind++);
					if(ind<datos.size())
					{
						anaAsig.Nuevo(Tipos[Integer.parseInt(datos.get(ind).getTip())]);//manda el valor numerico del tipo de datos a la Asignacion
						ntPos.Elemento(entraUser.get(pos));//manda el nombre de la id 
						temp = "";
					}
					
				}
				else
					if(ele.equals("num"))
					{
						anaAsig.Nuevo(entraUser.get(pos));//mandarlo igual para analisisAgignacion
						ntPos.Elemento(entraUser.get(pos));//mandarlo igual para notacionposfija
						temp = "";
					}
			}
			
		}

	}
	
	private void eleInter(String ele)//agrega elementos uno por uno al codigo intermedio
	{
		obd.Println(ele+"/"+entrada.get(contador));
		if(ele.equals(entrada.get(contador)))//pasa elementos para la notacionPosfija 
		{
			if(!ban)
			{
				codInter.Nuevo(entraUser.get(contador++));
			}
		}
	}

	private boolean ComprobarAsigna(String ele)
	{
		if((pilaT.getLast().equals("I21") && ele.equals(";")))
		{
			ban = false;
			if(anaAsig.Analizar())
			{
				this.Error(pos,"Error en la asignacion anaAsig");		
				return true;//retorna la confirmacion de error
			}else
			{
				obd.Println("la asignacion fue exitosa");
				codInter.Nuevos(ntPos.orden());//manda la Notacion Posfija al generador de codigo intermedio
				this.mostrarPilas(codInter.Elementos());//muestra los datos del codigo intermedio
				
				return false;//retorna la confirmacion de error
			}
		}
		else
			return false;
	}

	public LinkedList<String> Entrada(LinkedList<String> pila)
	{
		String temp="";
		int x;
		String tipod="", id="";
		boolean ban=false;
		LinkedList<String> pilatrans = new LinkedList<String>();
		/////////////////////////////////////////////////////////////////////	
		for(int y=0; y<pila.size(); y++)
		{
			//obd.Println(pila.get(y));
			if(pila.get(y).equals("float") || pila.get(y).equals("int") || pila.get(y).equals("char")) {
				tipod=pila.get(y);
				ban=true;
				}
			else if(pila.get(y).matches(expId) && ban && tablaSimbolos.indexOf(pila.get(y))==-1) {
					id=pila.get(y);
					tablaSimbolos.add(tipod);
					tablaSimbolos.add(id);
				}
			else if(pila.get(y).matches(";")) {
				ban=false;
				id=tipod="";
			}
			for(x=0;x<terminales.length && !terminales[x].equals(pila.get(y)) ;x++);
			if(x<terminales.length)
			{
				if(pila.get(y).equals(";"))
					temp = "";
				for(x=0;x<Tipos.length && !Tipos[x].equals(pila.get(y)) ;x++);
					if(x<Tipos.length)
						temp = pila.get(y);
				pilatrans.add(pila.get(y));
			}
			else
				if(pila.get(y).matches(expId))
				{
					for(x=0;x<datos.size() && !datos.get(x).getId().equals(pila.get(y));x++);
					if(temp!="")
					{
						if(x<datos.size())
						{
							pilatrans.add("null1");
						}
						else
						{
							dat = new DatosIDS();
							dat.setId(pila.get(y));
							for(x=0;x<Tipos.length && !Tipos[x].equals(temp);x++);
							if(x<Tipos.length)
								dat.setTip(x+"");//se guarda el tipo con su valor numerico si quisieramos guardarlo con su valor en cadena usaremos temp
							datos.add(dat);
							dat = null;
							pilatrans.add("id");
						}
					}
					else
					{
						if(x<datos.size())
						{
							pilatrans.add("id");
						}
						else
						{
							pilatrans.add("null0");
						}
					}			
				}
				else
					if(pila.get(y).matches(expNum))
						pilatrans.add("num");
					else
						if(pila.get(y).matches(elchar))
						{
							pilatrans.add("char");
						}
					else
						pilatrans.add("null");
		}
		pilatrans.add("$");
		this.mostrarPilas(pilatrans);
		entrada.addAll(pilatrans);//entrada es la pila modifica para usar la tabla sintactica
		return pilatrans;
		
	}
	
	public LinkedList<String> Separar(String txt)
	{
		txt+=" ";
		LinkedList<String> LisTemp = new LinkedList<String>();
		int temp;
		for(int x=0;x<txt.length();x++)
		{
			temp = txt.indexOf(" ",x);
			//obd.Println(temp+"");
			if(temp!=-1)  
				if(!txt.substring(x,temp).isBlank())
					 LisTemp.add(txt.substring(x,x=temp));
		}
		entraUser.addAll(LisTemp);//Entrada user es la pila como la entro el usuario
		return LisTemp;
			
	}
	
	public void mostrarPilas(LinkedList<String> pila)
	{
		System.out.println("Mostrar");
		for(int x = 0; x<pila.size();x++)
			System.out.println(pila.get(x));
	}
	
	public void mostrarSimbolos()
	{
		System.out.println("TABLA SIMBOLOS");
		for(int x = 0; x<tablaSimbolos.size();x++) {
			System.out.print(tablaSimbolos.get(x)+"\t");
			if(x%2!=0)
				System.out.println();
		}
	}
	
	
	public static void main(String[] args)
	{
		Principal obp = new Principal();
		//obp.Accion(obp.Entrada(obp.Separar("int id , id2 ; id = 5 * id ;")));obp.mostrarSimbolos();
                //obp.Accion(obp.Entrada(obp.Separar("int id , id2 ; id = 5 + id - id2 ;")));obp.mostrarSimbolos();
                 obp.Accion(obp.Entrada(obp.Separar("float id , id2 ; id = 5 * id / id2 ;")));obp.mostrarSimbolos();
		//obp.Accion(obp.Entrada(obp.Separar(" char id ; id = 5 + id - id2 * id / id2 ;    ")));obp.mostrarSimbolos();
	
        }

}
