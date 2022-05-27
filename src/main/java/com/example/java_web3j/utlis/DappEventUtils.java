package com.fingerchar.utils;

import com.example.java_web3j.pojo.EventValuesExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class DappEventUtils {

	private static final Logger logger = LoggerFactory.getLogger(com.fingerchar.utils.DappEventUtils.class);

	public static final Event BUY_EVENT = new Event("Buy", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}));
	public static final Event CANCEL_EVENT = new Event("Cancel", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}, new TypeReference<Address>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}));

	public static final Event TRANSFER_EVENT = new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}));

	public static final Event SECONDARYSALEFEES_EVENT = new Event("SecondarySaleFees",
			Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
			}, new TypeReference<DynamicArray<Address>>() {
			}, new TypeReference<DynamicArray<Uint256>>() {
			}));;

	public static final Event MARKETITEMCREATED_EVENT = new Event("MarketItemCreated",
			Arrays.<TypeReference<?>>asList(new TypeReference<Uint>(true) {
			}, new TypeReference<Address>(true) {
			}, new TypeReference<Uint256>(true) {
			}, new TypeReference<Address>() {
			}, new TypeReference<Address>() {
			}, new TypeReference<Uint256>() {
			}, new TypeReference<Uint8>() {
			}));

	public static final String TRANSFER_TOPIC = EventEncoder.encode(TRANSFER_EVENT);

	public static final String CANCEL_TOPIC = EventEncoder.encode(CANCEL_EVENT);

	public static final String BUY_TOPIC = EventEncoder.encode(BUY_EVENT);

	public static final String MARKETITEMCREATED_TOPIC = EventEncoder.encode(MARKETITEMCREATED_EVENT);

	public static final String SECONDARYSALEFEES_TOPIC = EventEncoder.encode(SECONDARYSALEFEES_EVENT);

	private static Web3j web3j = null;

	@SuppressWarnings("serial")
	public static final HashMap<String, Event> eventMap = new HashMap<String, Event>() {
		{
			put(TRANSFER_TOPIC, TRANSFER_EVENT);
			put(CANCEL_TOPIC, CANCEL_EVENT);
			put(BUY_TOPIC, BUY_EVENT);
			put(SECONDARYSALEFEES_TOPIC, SECONDARYSALEFEES_EVENT);
		}
	};

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, List<EventValuesExt>> decodeLog(List<LogResult> logList, Map<String, Event> eventMap) {
		Map<String, List<EventValuesExt>> map = new HashMap<>();
		if (null != logList && !logList.isEmpty()) {
			logList.stream().forEach(log-> {
				eventMap.keySet().stream().forEach(topic-> {
					if(((Log) log.get()).getTopics().contains(topic)) {

						if(null == map.get(eventMap.get(topic).getName())) {
							map.put(eventMap.get(topic).getName(), new ArrayList<>());
						}
						map.get(eventMap.get(topic).getName()).add(decodeLog(log, eventMap.get(topic)));
					}
				});
			});
		}
		return map;
	}

	public static EventValuesExt decodeLog(LogResult<Log> logResult, Event event) {
		EventValues eventValues = Contract.staticExtractEventParameters(event, logResult.get());
		BigInteger timestamp;
		try {
			timestamp = web3j.ethGetBlockByHash(logResult.get().getBlockHash(), false).send().getBlock().getTimestamp();
		} catch (IOException e) {
			throw new RuntimeException("get block timestamp error");
		}
		EventValuesExt val = new EventValuesExt(eventValues, logResult.get().getTransactionHash(), logResult.get().getAddress(),
				logResult.get().getBlockNumber(), timestamp);
		return val;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<EventValuesExt> decodeLog(List<LogResult> logList, String topic, Event event) {
		List<EventValuesExt> list = new ArrayList<>();
		if (null != logList && !logList.isEmpty()) {
			logList.stream().forEach(log-> {
				if(((Log) log.get()).getTopics().contains(topic)) {
					list.add(decodeLog(log, event));
				}
			});
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public static List<LogResult> getEthLogs(BigInteger start, BigInteger end, List<String> address) throws InterruptedException, ExecutionException, IOException {
		List<LogResult> list = new ArrayList<>();
		List<LogResult> temp = null;
		for(String addr : address) {
			temp = getEthLogs(start, end, addr);
			if(null != temp && !temp.isEmpty()) {
				list.addAll(temp);
			}
		}
		return list;

	}

	@SuppressWarnings("rawtypes")
	public static List<LogResult> getEthLogs(BigInteger start, BigInteger end, String address) throws InterruptedException, ExecutionException, IOException {
		EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end),
				address);
		EthLog log = web3j.ethGetLogs(filter).send();
		if(log.hasError()) {
			logger.error(log.getError().getMessage());
			throw new RuntimeException(log.getError().getMessage());
		} else {
			return log.getLogs();
		}
	}

	public static BigInteger getLastBlock() throws InterruptedException, ExecutionException, IOException {
		EthBlockNumber ebn = web3j.ethBlockNumber().send();
		if(ebn.hasError()) {
			throw new RuntimeException("get block number error");
		} else {
			return ebn.getBlockNumber();
		}
	}

	public static void initWeb3j(String url) {
		if (null == web3j) {
			web3j =  Web3j.build(new HttpService(url));
		}
	}

	public static void main(String[] args) {
		Web3j web3j = Web3j.build(new HttpService("https://rinkeby-light.eth.linkpool.io/"));
		try {
			BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
			System.out.println("当前区块："+blockNumber);

			EthFilter filter = new EthFilter(
					new DefaultBlockParameterNumber(10325891),
					new DefaultBlockParameterNumber(10325891),
					"0xf4f06F21D179F37387b2d4d4321297A36f3E7f8c");
			EthLog log = web3j.ethGetLogs(filter).send();
			List<LogResult> logs = log.getLogs();

			//这里只想获取这一个log日志，可以添加多个
			HashMap<String, Event> eventHashMap = new HashMap<String, Event>() {
				{
					put(MARKETITEMCREATED_TOPIC, MARKETITEMCREATED_EVENT);
				}
			};

			Map<String, List<EventValuesExt>> map = DappEventUtils.decodeLog(logs, eventHashMap);
			for (Map.Entry<String, List<EventValuesExt>> stringListEntry : map.entrySet()) {
				List<EventValuesExt> value = stringListEntry.getValue();
				for (EventValuesExt eventValuesExt : value) {
					System.out.println(eventValuesExt.toString());
				}
			}

			//核心方法就是这个 解析
			EventValues eventValues = Contract.staticExtractEventParameters(MARKETITEMCREATED_EVENT, (Log) logs.get(0).get());
			for (Type nonIndexedValue : eventValues.getNonIndexedValues()) {
				System.out.println(nonIndexedValue.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
