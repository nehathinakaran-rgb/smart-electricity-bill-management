const $ = id => document.getElementById(id);

const money = n => '₹' + (Number.isFinite(n) ? n : 0)
  .toLocaleString('en-IN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });

function calcEnergy(units) {
  let left = units, total = 0, lines = [];
  const over500 = units > 500;

  const slabs = over500
    ? [
        { cap: 100, rate: 0, label: 'First 100 units', free: true },
        { cap: 300, rate: 4.70, label: '101–400 units' },
        { cap: 100, rate: 6.30, label: '401–500 units' },
        { cap: 100, rate: 8.40, label: '501–600 units' },
        { cap: 200, rate: 9.45, label: '601–800 units' },
        { cap: Infinity, rate: null, label: 'Above 800 units' }
      ]
    : [
        { cap: 200, rate: 0, label: 'First 200 units', free: true },
        { cap: 200, rate: 4.70, label: '201–400 units' },
        { cap: 100, rate: 6.30, label: '401–500 units' }
      ];

  for (const s of slabs) {
    if (left <= 0) break;

    const n = Math.min(left, s.cap);

    if (s.rate === null) {
      return { known: false, total, lines, unpriced: n };
    }

    total += n * s.rate;
    lines.push({
      label: s.label,
      units: n,
      rate: s.rate,
      amount: n * s.rate,
      free: !!s.free
    });
    left -= n;
  }

  return { known: true, total, lines, unpriced: 0 };
}

function setError(msg) {
  $('error').textContent = msg;
  $('error').classList.toggle('show', !!msg);
}

function generate() {
  const name = $('consumer').value.trim();
  const prevText = $('previous').value;
  const curText = $('current').value;
  const prev = Number(prevText);
  const cur = Number(curText);

  if (!name) {
    setError('Please enter the consumer name.');
    return;
  }

  if (
    prevText === '' || curText === '' ||
    !Number.isFinite(prev) || !Number.isFinite(cur) ||
    prev < 0 || cur < prev
  ) {
    setError('Enter valid meter readings. The current reading must be greater than or equal to the previous reading.');
    return;
  }

  const usage = cur - prev;

  if (!Number.isInteger(usage)) {
    setError('Please use whole-number meter readings for this demo.');
    return;
  }

  const calc = calcEnergy(usage);
  const free = usage <= 500
    ? Math.min(usage, 200)
    : Math.min(usage, 100);

  setError('');

  $('usageMetric').textContent =
    usage.toLocaleString('en-IN') + ' kWh';
  $('freeMetric').textContent = free + ' units';
  $('energyMetric').textContent =
    money(calc.total) + (calc.known ? '' : '*');
  $('totalMetric').textContent =
    calc.known ? money(calc.total) : 'Verify rate';

  $('billResult').textContent = calc.known
    ? money(calc.total)
    : money(calc.total) + ' + unpriced units';

  $('billStatus').textContent =
    calc.known ? 'BILL GENERATED' : 'PARTIAL ESTIMATE';

  $('unitsResult').textContent = usage + ' units';
  $('freeResult').textContent = free + ' units';
  $('chargeableResult').textContent =
    Math.max(0, usage - free) + ' units';

  $('energyResult').textContent =
    money(calc.total) +
    (calc.known ? '' : ' + unpriced slab');

  const dt = $('billDate').value
    ? new Date($('billDate').value + 'T12:00:00')
    : new Date();

  const dateLabel = dt.toLocaleDateString('en-IN', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });

  const billNo = 'EB-' +
    dt.getFullYear() +
    String(dt.getMonth() + 1).padStart(2, '0') +
    String(dt.getDate()).padStart(2, '0') +
    '-' + String(usage).padStart(4, '0');

  $('receiptNo').textContent = billNo;
  $('receiptDate').textContent = dateLabel;
  $('receiptName').textContent = name;
  $('receiptConsumerNo').textContent =
    $('consumerNo').value.trim() || 'Not provided';
  $('receiptPeriod').textContent = $('period').value;
  $('receiptReadings').textContent = prev + ' → ' + cur;
  $('receiptUnits').textContent = usage + ' units';
  $('receiptEnergy').textContent =
    money(calc.total) + (calc.known ? '' : ' (partial)');
  $('receiptFree').textContent = free + ' units';

  $('receiptTotal').textContent = calc.known
    ? money(calc.total)
    : money(calc.total) + ' + unpriced slab';

  $('receipt').classList.add('show');

  [100, 200, 500, 800].forEach(t => {
    $('bar' + t).style.width =
      Math.min(100, usage / t * 100) + '%';
    $('pct' + t).textContent =
      Math.round(usage / t * 100) + '%';
  });

  if (!calc.known) {
    setError(
      'Your usage exceeds 800 units. The verified consumer-pay rate above 800 was not confirmed for this demo, so the receipt shows a partial estimate. Check the current TNERC/TNPDCL schedule.'
    );
  }
}

$('generate').addEventListener('click', generate);

$('print').addEventListener('click', () => window.print());

$('reset').addEventListener('click', () => {
  $('consumer').value = 'Sample Consumer';
  $('consumerNo').value = '';
  $('previous').value = '';
  $('current').value = '';
  $('billDate').value = new Date().toISOString().slice(0, 10);
  $('period').selectedIndex = 0;
  $('receipt').classList.remove('show');

  ['usageMetric', 'freeMetric', 'energyMetric', 'totalMetric']
    .forEach(id => $(id).textContent = '—');

  $('billResult').textContent = '₹—';
  $('billStatus').textContent = 'AWAITING INPUT';

  ['unitsResult', 'freeResult', 'chargeableResult', 'energyResult']
    .forEach(id => $(id).textContent = '—');

  [100, 200, 500, 800].forEach(t => {
    $('bar' + t).style.width = '0';
    $('pct' + t).textContent = '—';
  });

  setError('');
});

$('billDate').value = new Date().toISOString().slice(0, 10);
generate();
