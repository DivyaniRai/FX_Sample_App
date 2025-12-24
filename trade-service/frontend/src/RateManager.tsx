import React, {useEffect, useState} from 'react';
import axios from 'axios';

export default function RateManager(){
  const [pairs,setPairs]=useState<string[]>([]);
  const [pair,setPair]=useState('EURUSD');
  const [rate,setRate]=useState('1.1');

  const load = async ()=>{
    const resp = await axios.get('/api/ref/pairs', {headers:{'X-API-KEY':'test-key'}});
    setPairs(resp.data || []);
  }

  useEffect(()=>{ load() },[]);

  const get = async ()=>{
    const r = await axios.get('/api/rates/'+pair, {headers:{'X-API-KEY':'test-key'}});
    setRate(r.data);
  }

  const setR = async ()=>{
    await axios.post('/api/rates?pair='+pair+'&rate='+rate, null, {headers:{'X-API-KEY':'test-key'}});
    alert('updated');
  }

  return (<div>
    <h3>Rates</h3>
    <div>
      <select value={pair} onChange={e=>setPair(e.target.value)}>
        {pairs.map(p=> <option key={p} value={p}>{p}</option>)}
      </select>
      <button onClick={get}>Get</button>
    </div>
    <div>
      <input value={rate} onChange={e=>setRate(e.target.value)} />
      <button onClick={setR}>Set</button>
    </div>
  </div>)
}
