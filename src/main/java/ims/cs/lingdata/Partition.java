
/*
 * This file is part of QSample.
 * QSample is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QSample is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QSample.  If not, see <http://www.gnu.org/licenses/>.
 */


package ims.cs.lingdata;

import java.util.List;
import java.util.Map;

/**
 * A partition is a list of documents which may belong to different sections
 */
public class Partition  {

	public List<Document> docList;
	public Map<String, List<Document>> sectionMap;
	

	public Partition() {}
	
	public Partition(List<Document> docList) {
		this.docList = docList;
	}


	public List<Document> getDocumentList() {
		return docList;
	}
	
	public int size() {
		return docList.size();
	}


}