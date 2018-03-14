public class MyHashTable<AnyType>
{
   
    public MyHashTable( ){
        this( DEFAULT_TABLE_SIZE );
    }

    public MyHashTable( int size ){
        allocateArray( size );
        doClear( );
    }

  
    public boolean insert( AnyType x , boolean isPrefix){
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
            return false;

        if( array[ currentPos ] == null )
            ++occupied;
        array[ currentPos ] = new HashEntry<>( x, true, isPrefix);
        theSize++;
        
        if( occupied > array.length / 2 )
            rehash( );
        
        return true;
    }

    
    private void rehash( ){
        HashEntry<AnyType> [ ] oldArray = array;
        allocateArray( 2 * oldArray.length );
        occupied = 0;
        theSize = 0;

        for( HashEntry<AnyType> entry : oldArray )
            if( entry != null && entry.isActive )
                insert( entry.element, entry.isPrefix);
   }

    private int findPos( AnyType x ){
        int currentPos = myhash( x );
        
        while( array[ currentPos ] != null &&
                !array[ currentPos ].element.equals( x ) )
        {
            currentPos = currentPos + 1;  // Compute ith probe
            if( currentPos >= array.length )
                currentPos -= array.length;
        }
        
        return currentPos;
    }

    public boolean remove( AnyType x ){
        int currentPos = findPos( x );
        if( isActive( currentPos ) )
        {
            array[ currentPos ].isActive = false;
            theSize--;
            return true;
        }
        else
            return false;
    }
    
    public int size( ){
        return theSize;
    }
    
    public int capacity( ){
        return array.length;
    }

    public boolean contains( AnyType x ){
        int currentPos = findPos( x );
        return isActive( currentPos );
    }

    private boolean isActive( int currentPos ){
        return array[ currentPos ] != null && array[ currentPos ].isActive;
    }

    public void makeEmpty( ){
        doClear( );
    }

    private void doClear( ){
        occupied = 0;
        for( int i = 0; i < array.length; i++ )
            array[ i ] = null;
    }
    
    private int myhash( AnyType x ){
        int hashVal = x.hashCode( );

        hashVal %= array.length;
        if( hashVal < 0 )
            hashVal += array.length;

        return hashVal;
    }
    
    public boolean isPrefix( AnyType x ){
        int currentPos = findPos( x );
        return array[ currentPos ].isPrefix;
    }

    private void allocateArray( int arraySize ){
        array = new HashEntry[ nextPrime( arraySize ) ];
    }

    private static int nextPrime( int n ){
        if( n % 2 == 0 )
            n++;

        for( ; !isPrime( n ); n += 2 )
            ;

        return n;
    }


    private static boolean isPrime( int n ){
        if( n == 2 || n == 3 )
            return true;

        if( n == 1 || n % 2 == 0 )
            return false;

        for( int i = 3; i * i <= n; i += 2 )
            if( n % i == 0 )
                return false;

        return true;
    }
    
    private static class HashEntry<AnyType>{
        public AnyType  element;   // the element
        public boolean isActive;  // false if marked deleted
        public boolean isPrefix; 
        
        public HashEntry( AnyType e, boolean i, boolean j )
        {
            element  = e;
            isActive = i;
            isPrefix = j;
        }
    }
    
    private static final int DEFAULT_TABLE_SIZE = 101;
    private HashEntry<AnyType> [ ] array; 
    private int occupied;             
    private int theSize;   
}
