package model.eng;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class MethodPredicates extends PredicatesCollection<Method>
{
	@Override
	public boolean test(Method method)
	{
		Collection<Boolean> bools;
		boolean result;
		
		bools = new ArrayList<>();
		for (Predicate<Method> predicate : this.predicates)
		{
			bools.add(predicate.test(method));
		}
		result = FieldPredicates.and(bools);
		
		return result;
	}
	
	public static final class PublicPredicate implements Predicate<Method>
	{
		private boolean isPublic;
		public PublicPredicate(boolean isPublic)
		{
			this.isPublic = isPublic;
		}
		
		@Override
		public boolean test(Method method)
		{
			int modifiers;
			boolean result;
			
			modifiers = method.getModifiers();
			result = Modifier.isPublic(modifiers);
			if (!this.isPublic)
			{
				result = !result;
			}
			return result;
		}
	}
	
	public static final class ProtectedPredicate implements Predicate<Method>
	{
		private boolean isProtected;
		public ProtectedPredicate(boolean isProtected)
		{
			this.isProtected = isProtected;
		}
		
		@Override
		public boolean test(Method method)
		{
			int modifiers;
			boolean result;
			
			modifiers = method.getModifiers();
			result = Modifier.isProtected(modifiers);
			if (!this.isProtected)
			{
				result = !result;
			}
			return result;
		}
	}
	
	public static final class MethodNthArgumentsPredicate implements Predicate<Method>
	{
		private final int parameterCount;
		public MethodNthArgumentsPredicate(int parameterCount)
		{
			this.parameterCount = parameterCount;
		}
		
		@Override
		public boolean test(Method method)
		{
			int parameterCount;
			boolean result;
			
			parameterCount = method.getParameterCount();
			result = (this.parameterCount == parameterCount);
			
			return result;
		}
		
	};
	
	public static final class MethodNamePredicate implements Predicate<Method>
	{
		private final Predicate<String> predicate;
		public MethodNamePredicate(Predicate<String> predicate)
		{
			this.predicate = predicate;
		}
		@Override
		public boolean test(Method method)
		{
			String methodName;
			boolean result;
			
			methodName = method.getName();
			result = this.predicate.test(methodName);
			
			return result;
		}
		
	}
	
	public static final class MethodReturnTypePredicate implements Predicate<Method>
	{
		private final Predicate<Class<?>> predicate;
		public MethodReturnTypePredicate(Predicate<Class<?>> predicate)
		{
			this.predicate = predicate;
		}
		@Override
		public boolean test(Method method)
		{
			Class<?> returnType;
			boolean result;
			
			returnType = method.getReturnType();
			result = this.predicate.test(returnType);
			
			return result;
		}
		
	}
	
	public static final class ProcedurePredicate implements Predicate<Method>
	{
		private final MethodReturnTypePredicate predicate;
		public ProcedurePredicate()
		{
			this.predicate = new MethodReturnTypePredicate(new Predicate<Class<?>>()
			{

				@Override
				public boolean test(Class<?> returnType)
				{
					boolean result;
					
					result = returnType.equals(Void.TYPE);
					
					return result;
				}
				
			});
		}
		@Override
		public boolean test(Method method)
		{
			boolean result;
			
			result = this.predicate.test(method);
			
			return result;
		}
		
	}

	public static final class FunctionPredicate implements Predicate<Method>
	{
		private final MethodReturnTypePredicate predicate;
		public FunctionPredicate()
		{
			this.predicate = new MethodReturnTypePredicate(new Predicate<Class<?>>()
			{

				@Override
				public boolean test(Class<?> returnType)
				{
					boolean result;
					
					result = !returnType.equals(Void.TYPE);
					
					return result;
				}
				
			});
		}
		@Override
		public boolean test(Method method)
		{
			boolean result;
			
			result = this.predicate.test(method);
			
			return result;
		}
		
	}

	public static final class PreffixNamePredicate implements Predicate<String>
	{
		private final String prefixName;
		public PreffixNamePredicate(String prefixName)
		{
			this.prefixName = prefixName;
		}
		@Override
		public boolean test(String name)
		{
			boolean result;
			
			result = name.startsWith(this.prefixName);
			
			return result;
		}
		
	}
	
	public static final class SuffixNamePredicate implements Predicate<String>
	{
		private final String suffixName;
		public SuffixNamePredicate(String suffixName)
		{
			this.suffixName = suffixName;
		}
		@Override
		public boolean test(String name)
		{
			boolean result;
			
			result = name.endsWith(this.suffixName);
			
			return result;
		}
		
	}

}
