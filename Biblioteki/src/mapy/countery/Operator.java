package mapy.countery;

public interface Operator<T>
{
	T update(T a, T b);
	T subtract(T a, T b);
	
	public static final Operator<Integer> operatorInteger = new Operator<Integer>(){

		@Override
		public Integer update(Integer a, Integer b)
		{
			return a + b;
		}

		@Override
		public Integer subtract(Integer a, Integer b)
		{
			return a - b;
		}
		
	};
	
	public static final Operator<Float> operatorFloat = new Operator<Float>(){

		@Override
		public Float update(Float a, Float b)
		{
			return a + b;
		}

		@Override
		public Float subtract(Float a, Float b)
		{
			return a - b;
		}
		
	};
	
	public static final Operator<Double> operatorDouble = new Operator<Double>(){

		@Override
		public Double update(Double a, Double b)
		{
			return a + b;
		}

		@Override
		public Double subtract(Double a, Double b)
		{
			return a - b;
		}
		
	};

	public static final Operator<Number> operatorNumber	= new Operator<Number>(){

		@Override
		public Number update(Number a, Number b)
		{
			return a.doubleValue() + b.doubleValue();
		}

		@Override
		public Number subtract(Number a, Number b)
		{
			return a.doubleValue() - b.doubleValue();
		}
		
	};
}
