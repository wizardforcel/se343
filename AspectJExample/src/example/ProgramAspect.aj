package example;

public aspect ProgramAspect 
{
	pointcut main() : execution(* Program.main(..));
	
	pointcut getResult(int i) : 
		execution(* Queens.getResult(int)) && args(i);

	pointcut getResultCount() : execution(* Queens.getResultCount());

	pointcut getSize() : execution(* Queens.getSize());
	
	pointcut generate(int i) : 
		execution(* Queens.generate(int)) && args(i);
	
	pointcut rGenerate(int i) :
		execution(* Queens.rGenerate(int)) && args(i);
	
	pointcut copyBoard() : execution(* Queens.copyBoard());
	
	pointcut canSet(int i, int j) : 
		execution(* Queens.canSet(int, int)) && args(i, j);
	
	before() : main()
	{
		System.out.println("before calling Program.main");
	}
	
	after(): main()
	{
		System.out.println("after calling Program.main");
	}
	
	before(int i) : getResult(i)
	{
		System.out.println("before calling Queens.getResult(" + i + ")");
	}
	
	after(int i) : getResult(i)
	{
		System.out.println("after calling Queens.getResult(" + i + ")");
	}
	
	before(int i) : generate(i)
	{
		System.out.println("before calling Queens.generate(" + i + ")");
	}
	
	after(int i) : generate(i)
	{
		System.out.println("after calling Queens.generate(" + i + ")");
	}
	
	before(int i) : rGenerate(i)
	{
		System.out.println("before calling Queens.rGenerate(" + i + ")");
	}
	
	after(int i) : rGenerate(i)
	{
		System.out.println("after calling Queens.rGenerate(" + i + ")");
	}
	
	before() : getResultCount()
	{
		System.out.println("before calling Queens.getResultCount");
	}
	
	after() : getResultCount()
	{
		System.out.println("after calling Queens.getResultCount");
	}
	
	before() : getSize()
	{
		System.out.println("before calling Queens.getSize");
	}
	
	after() : getSize()
	{
		System.out.println("after calling Queens.getSize");
	}
	
	before() : copyBoard()
	{
		System.out.println("before calling Queens.copyBoard");
	}
	
	after() : copyBoard()
	{
		System.out.println("after calling Queens.copyBoard");
	}
	
	before(int i, int j) : canSet(i, j)
	{
		System.out.println("before calling Queens.canSet(" + i + ", " + j + ")");
	}
	
	after(int i, int j) : canSet(i, j)
	{
		System.out.println("after calling Queens.canSet(" + i + ", " + j + ")");
	}
}
