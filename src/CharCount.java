/*

  Authors (group members): Connor DuBois, Alexander Hsieh, Zachary Paryzek, John Autry
  Email addresses of group members: cdubois2014@my.fit.edu, Ahsieh2014@my.fit.edu, jautry2016@my.fit.edu, zparyzek2012@my.fit.edu
  Group name: 23D

  Course: CSE2010
  Section: 3

  Description of the overall algorithm:


*/

public class CharCount implements Comparable<CharCount>
{
	public char letter;
	public int count;
	
	public CharCount(char letter)
	{
		this.letter = letter;
		this.count = 0;
	}

	@Override
	public int compareTo(CharCount c)
	{
		//if this count is greater, return 1
		if(count < c.count)
		{
			return 1;
		}
		
		//if this count is less, return -1
		else if(count > c.count)
		{
			return -1;
		}
		
		//if equal return 0
		else
		{
			return 0;
		}
	}
	
	@Override
	public boolean equals(Object o)
	{
		CharCount c = (CharCount) o;
		
		//if letter is equal, return true
		if(letter == c.letter)
		{
			return true;
		}

		//if not return 0
		else
		{
			return false;
		}
	}
	
	
	@Override
	public String toString()
	{
		return "*letter=" + letter + ", count=" + count + "*"; 
	}
}
