import http from 'k6/http';
import { check } from 'k6';
import { sleep } from 'k6';

export const options = {
    thresholds: {
        http_req_failed: ['rate<0.01'], // http errors should be less than 1%
        http_req_duration: ['p(95)<200'], // 95% of requests should be below 200ms
    },
    discardResponseBodies: true,
    scenarios: {
        contacts: {
            executor: 'ramping-vus',
            startVUs: 0,
            stages: [
                { duration: '1m', target: 1000 },
                { duration: '1m', target: 2000 },
                { duration: '1m', target: 3000 },
                { duration: '1m', target: 4000 },
                { duration: '1m', target: 5000 },
                { duration: '1m', target: 6000 },
                { duration: '1m', target: 7000 },
                { duration: '1m', target: 8000 },
                { duration: '30s', target: 8000 },
                { duration: '10s', target: 0 },
            ],
            gracefulRampDown: '0s',
        },
    },
};


export default function () {
    const res = http.get('http://node28791-env-6815870.rag-cloud.hosteur.com/dev-poc-siu/hospitals/51.437195/-2.847193');
    check(res, {
      'is status 200': (r) => r.status === 200,
    });
    // Virtual User make 4 request per second
    sleep(0.5);
}

