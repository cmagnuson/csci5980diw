/*  This file is part of GRender.
 *
 *  Send feedback and check for updates on on http://fluxcapacity.net
 *  
 *  GRender is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation; either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   GRender is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

using System;
using System.Collections.Generic;
using System.IO;

namespace Sys.IO
{
    public static class File
    {
        /// <summary>
        /// (Supplemented) Reads all lines from a stream.
        /// </summary>
        /// <param name="s">The stream to read.</param>
        /// <returns>A string array containing all lines of the file.</returns>
        public static string[] ReadAllLines(Stream s)
        {
            System.IO.StreamReader reader = new StreamReader(s);
            List<string> allLines = new List<string>();

            while(!reader.EndOfStream)
            {
                allLines.Add(reader.ReadLine());
            }
            return allLines.ToArray();
        }
    }


}
