import React, {useEffect, useState} from 'react';
import axios from 'axios';

export default function Blotter(){
  const [trades,setTrades]=useState<any[]>([]);

  const load = async ()=>{
    const resp = await axios.get('/api/trades', {headers:{'X-API-KEY':'test-key'}});
    setTrades(resp.data || []);
  }

  useEffect(()=>{ load() },[]);

  return (<div>
    <h3>Blotter</h3>
    <button onClick={load}>Refresh</button>
    <table border={1} style={{width:'100%', marginTop:8}}>
      <thead><tr><th>ID</th><th>Type</th><th>Buy/Sell</th><th>Amounts</th><th>Status</th></tr></thead>
      <tbody>
        {trades.map(t=> (<tr key={t.id}><td>{t.id}</td><td>{t.tradeType}</td><td>{t.buyCurrency}/{t.sellCurrency}</td><td>{t.buyAmount}/{t.sellAmount}</td><td>{t.status}</td></tr>))}
      </tbody>
    </table>
  </div>)
}
