import React, {useEffect, useState} from 'react';
import axios from 'axios';

export default function Blotter(){
  const [trades,setTrades]=useState<any[]>([]);
  const [customerId,setCustomerId]=useState<string>('');
  const [customers,setCustomers]=useState<any[]>([]);
  const [tradeType,setTradeType]=useState<string>('');
  const [status,setStatus]=useState<string>('');
  const [tradeDateFrom,setTradeDateFrom]=useState<string>('');
  const [tradeDateTo,setTradeDateTo]=useState<string>('');
  const [selectedId,setSelectedId]=useState<number | null>(null);
  const [confirmationText,setConfirmationText]=useState<string | null>(null);
  const [showConfirmation,setShowConfirmation]=useState<boolean>(false);

  const buildParams = ()=>{
    const p:any = {};
    if(customerId) p.customerId = customerId;
    if(tradeType) p.tradeType = tradeType;
    if(status) p.status = status;
    if(tradeDateFrom) p.tradeDateFrom = tradeDateFrom;
    if(tradeDateTo) p.tradeDateTo = tradeDateTo;
    return p;
  }

  const load = async ()=>{
    try{
      const resp = await axios.get('/api/trades', {params: buildParams(), headers:{'X-API-KEY':'test-key'}});
      setTrades(resp.data || []);
    }catch(e:any){
      alert('Failed to load trades: ' + (e.message || e));
    }
  }

  const fetchCustomers = async ()=>{
    try{
      const r = await axios.get('/api/customers', {headers:{'X-API-KEY':'test-key'}});
      setCustomers(r.data || []);
    }catch(e:any){
      console.warn('Failed to fetch customers: ', e);
    }
  }

  useEffect(()=>{ load(); fetchCustomers(); },[]);

  const clearFilters = ()=>{
    setCustomerId(''); setTradeType(''); setStatus(''); setTradeDateFrom(''); setTradeDateTo('');
    load();
  }

  const handleConfirm = async (id:number)=>{
    if(!window.confirm('Confirm trade ' + id + '?')) return;
    try{
      await axios.post(`/api/trades/${id}/confirm`, null, {headers:{'X-API-KEY':'test-key'}});
      alert('Trade confirmed');
      load();
    }catch(e:any){
      alert('Confirm failed: ' + (e.response?.data?.message || e.message || e));
    }
  }

  const handleCancel = async (id:number)=>{
    if(!window.confirm('Cancel trade ' + id + '?')) return;
    try{
      await axios.post(`/api/trades/${id}/cancel`, null, {headers:{'X-API-KEY':'test-key'}});
      alert('Trade cancelled');
      load();
    }catch(e:any){
      alert('Cancel failed: ' + (e.response?.data?.message || e.message || e));
    }
  }

  const viewConfirmation = async (tradeId:number)=>{
    try{
      const resp = await axios.get(`/api/confirmations/by-trade/${tradeId}`, {headers:{'X-API-KEY':'test-key'}});
      setConfirmationText(resp.data.confirmationText || JSON.stringify(resp.data));
      setShowConfirmation(true);
    }catch(e:any){
      // if not found, prompt to generate
      const ok = window.confirm('No confirmation found. Generate one now?');
      if(ok){
        try{
          const r2 = await axios.post(`/api/confirmations/generate?tradeId=${tradeId}`, null, {headers:{'X-API-KEY':'test-key'}});
          setConfirmationText(r2.data.confirmationText || JSON.stringify(r2.data));
          setShowConfirmation(true);
        }catch(err:any){
          alert('Failed to generate confirmation: ' + (err.response?.data?.message || err.message || err));
        }
      }
    }
  }

  return (<div>
    <h3>Blotter</h3>
    <div style={{display:'flex',gap:8,alignItems:'center',marginBottom:8}}>
      <select value={customerId} onChange={e=>setCustomerId(e.target.value)}>
        <option value="">All Customers</option>
        {customers.map(c=> (<option key={c.id} value={c.id}>{c.id}{c.name? ' - ' + c.name: ''}</option>))}
      </select>

      <select value={tradeType} onChange={e=>setTradeType(e.target.value)}>
        <option value="">All Types</option>
        <option value="SPOT">SPOT</option>
        <option value="FORWARD">FORWARD</option>
      </select>
      <select value={status} onChange={e=>setStatus(e.target.value)}>
        <option value="">All Statuses</option>
        <option value="NEW">NEW</option>
        <option value="CONFIRMED">CONFIRMED</option>
        <option value="CANCELLED">CANCELLED</option>
      </select>
      <label style={{display:'flex',flexDirection:'column'}}>
        From
        <input type="date" value={tradeDateFrom} onChange={e=>setTradeDateFrom(e.target.value)} />
      </label>
      <label style={{display:'flex',flexDirection:'column'}}>
        To
        <input type="date" value={tradeDateTo} onChange={e=>setTradeDateTo(e.target.value)} />
      </label>
      <button onClick={load}>Search</button>
      <button onClick={clearFilters}>Clear</button>
      <button onClick={load}>Refresh</button>
    </div>

    <table border={1} style={{width:'100%', marginTop:8,borderCollapse:'collapse'}}>
      <thead>
        <tr style={{background:'#eee'}}>
          <th>ID</th><th>Customer</th><th>Type</th><th>Buy/Sell</th><th>Amounts</th><th>Trade Date</th><th>Status</th><th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {trades.map((t)=>{
          const isSelected = selectedId === t.id;
          return (<tr key={t.id} onClick={()=>setSelectedId(t.id)} style={{background:isSelected? '#def':'transparent', cursor:'pointer'}}>
            <td style={{padding:6}}>{t.id}</td>
            <td>{t.customerId}</td>
            <td>{t.tradeType}</td>
            <td>{t.buyCurrency}/{t.sellCurrency}</td>
            <td>{t.buyAmount}/{t.sellAmount}</td>
            <td>{t.tradeDate}</td>
            <td>{t.status}</td>
            <td>
              <button disabled={t.status !== 'NEW'} onClick={(e)=>{e.stopPropagation(); handleConfirm(t.id)}}>Confirm</button>
              <button disabled={t.status === 'CANCELLED'} onClick={(e)=>{e.stopPropagation(); handleCancel(t.id)}} style={{marginLeft:8}}>Cancel</button>
              <button onClick={(e)=>{e.stopPropagation(); viewConfirmation(t.id)}} style={{marginLeft:8}}>View Confirmation</button>
            </td>
          </tr>)
        })}
      </tbody>
    </table>

    {showConfirmation && (
      <div style={{position:'fixed',left:0,top:0,right:0,bottom:0,background:'rgba(0,0,0,0.3)',display:'flex',alignItems:'center',justifyContent:'center'}} onClick={()=>setShowConfirmation(false)}>
        <div style={{background:'white',padding:20,maxWidth:800}} onClick={(e)=>e.stopPropagation()}>
          <h4>Confirmation</h4>
          <pre style={{whiteSpace:'pre-wrap'}}>{confirmationText}</pre>
          <div style={{textAlign:'right',marginTop:12}}>
            <button onClick={()=>setShowConfirmation(false)}>Close</button>
          </div>
        </div>
      </div>
    )}

  </div>)
}
