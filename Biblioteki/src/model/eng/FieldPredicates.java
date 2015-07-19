package model.eng;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class FieldPredicates extends PredicatesCollection<Field> implements Predicate<Field>
{

	@Override
	public boolean test(Field field)
	{
		Collection<Boolean> bools;
		boolean result;
		
		bools = new ArrayList<>();
		for (Predicate<Field> predicate : this.predicates)
		{
			bools.add(predicate.test(field));
		}
		result = and(bools);
		
		return result;
	}
	
	public static final class PublicPredicate implements Predicate<Field>
	{
		private boolean isPublic;
		public PublicPredicate(boolean isPublic)
		{
			this.isPublic = isPublic;
		}
		
		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isPublic(modifiers);
			if (!this.isPublic)
			{
				result = !result;
			}
			return result;
		}
	}
	
	public static final class ProtectedPredicate implements Predicate<Field>
	{
		private boolean isProtected;
		public ProtectedPredicate(boolean isProtected)
		{
			this.isProtected = isProtected;
		}
		
		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isProtected(modifiers);
			if (!this.isProtected)
			{
				result = !result;
			}			
			return result;
		}
	}
	
	public static final class StaticPredicate implements Predicate<Field>
	{
		private boolean isStatic;
		public StaticPredicate(boolean isStatic)
		{
			this.isStatic = isStatic;
		}

		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isStatic(modifiers);
			if (!this.isStatic)
			{
				result = !result;
			}
			
			return result;
		}
	}
	
	public static final class FinalPredicate implements Predicate<Field>
	{
		private boolean isFinal;
		public FinalPredicate(boolean isFinal)
		{
			this.isFinal = isFinal;
		}

		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isFinal(modifiers);
			if (!this.isFinal)
			{
				result = !result;
			}
			
			return result;
		}
	}
	
	public static final class PrivatePredicate implements Predicate<Field>
	{
		private boolean isPrivate;
		public PrivatePredicate(boolean isPrivate)
		{
			this.isPrivate = isPrivate;
		}

		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isPrivate(modifiers);
			if (!this.isPrivate)
			{
				result = !result;
			}
			
			return result;
		}
	}
	
	public static final class TransientPredicate implements Predicate<Field>
	{
		private boolean isTransient;
		public TransientPredicate(boolean isTransient)
		{
			this.isTransient = isTransient;
		}

		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = Modifier.isTransient(modifiers);
			if (!this.isTransient)
			{
				result = !result;
			}
			
			return result;
		}
	}
	
	public static final class VolatilePredicate implements Predicate<Field>
	{
		private boolean isVolatile;
		public VolatilePredicate(boolean isVolatile)
		{
			this.isVolatile = isVolatile;
		}

		@Override
		public boolean test(Field field)
		{
			int modifiers;
			boolean result;
			
			modifiers = field.getModifiers();
			result = !Modifier.isVolatile(modifiers);
			if (!this.isVolatile)
			{
				result = !result;
			}
			
			return result;
		}
	}
}
