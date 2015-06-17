package protocolchat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CrazyMap<K, V> extends HashMap<K, V> {
	
	//删除指定项
	public void removeByValue(Object value){
		for(Object obj : keySet()){
			if(get(obj) == value){
				remove(obj);
				break;
			}
		}
	}
	
	//获取所有value组成的Set集合
	public Set<V> valueSet(){
		Set<V> result = new HashSet<>();
		for(K key : keySet()){
			result.add(get(key));
		}
		return result;
	}

	//根据key查找value
	public K getKeyByValue(V val){
		//遍历所有key组成的集合
		for(K key : keySet()){
			//如果指定key对应的value与被搜索的value相同，则返回相应的key
			if(get(key).equals(val) && get(key)==val){
				return key;
			}
		}
		return null;
	}
	
	//重写HashMap的put方法
	public V put(K key, V value){
		for(V val : valueSet()){
			//不许有重复的value
			if(val.equals(value) && val.hashCode() == value.hashCode()){
				throw new RuntimeException("Map中不允许有重复的value!!!");
			}
		}
		return super.put(key, value);
	}
}
