package com.kercer.kerkeesdk.util;

import com.kercer.kercore.debug.KCLog;

public class KCUtilVersion
{

	public static int compareVersion(String a, String b)
	{
		int res = 0;
		try
		{
			String[] aNumber = a.split("\\.");
			String[] bNumber = b.split("\\.");
			int maxIndex = Math.max(aNumber.length, bNumber.length);

			for (int i = 0; i < maxIndex; i++)
			{
				int aVersionPart = i < aNumber.length ? Integer.parseInt(aNumber[i]) : 0;
				int bVersionPart = i < bNumber.length ? Integer.valueOf(bNumber[i]) : 0;

				if (aVersionPart < bVersionPart)
				{
					res = -1;
					break;
				}
				else if (aVersionPart > bVersionPart)
				{
					res = 1;
					break;
				}
			}
		}
		catch (Exception e)
		{
			KCLog.e(e);
		}
		return res;
	}

}
