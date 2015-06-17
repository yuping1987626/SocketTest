package protocolchat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CrazyMap<K, V> extends HashMap<K, V> {
	
	//ɾ��ָ����
	public void removeByValue(Object value){
		for(Object obj : keySet()){
			if(get(obj) == value){
				remove(obj);
				break;
			}
		}
	}
	
	//��ȡ����value��ɵ�Set����
	public Set<V> valueSet(){
		Set<V> result = new HashSet<>();
		for(K key : keySet()){
			result.add(get(key));
		}
		return result;
	}

	//����key����value
	public K getKeyByValue(V val){
		//��������key��ɵļ���
		for(K key : keySet()){
			//���ָ��key��Ӧ��value�뱻������value��ͬ���򷵻���Ӧ��key
			if(get(key).equals(val) && get(key)==val){
				return key;
			}
		}
		return null;
	}
	
	//��дHashMap��put����
	public V put(K key, V value){
		for(V val : valueSet()){
			//�������ظ���value
			if(val.equals(value) && val.hashCode() == value.hashCode()){
				throw new RuntimeException("Map�в��������ظ���value!!!");
			}
		}
		return super.put(key, value);
	}
}
